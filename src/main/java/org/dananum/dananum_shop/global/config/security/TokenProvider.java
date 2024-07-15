package org.dananum.dananum_shop.global.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.config.redis.entity.RefreshToken;
import org.dananum.dananum_shop.global.config.redis.repository.RefreshTokenRepository;
import org.dananum.dananum_shop.global.config.redis.service.TokenService;
import org.dananum.dananum_shop.global.util.JwtTokenUtil;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "Authorization";
    private static final int ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    private final Key key;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    //암호화 키값 생성
    public TokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, TokenService tokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰 생성
    public void generateTokenDto(Authentication authentication, HttpServletResponse httpServletResponse) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access RefreshToken 생성
        // Access Token에만 유저정보 담음
        String accessToken = JwtTokenUtil.createAccessToken(authorities, authentication.getName(), key);

        // Refresh RefreshToken 생성
        // 만료일자 외 다른 정보 필요없음
        String refreshToken = JwtTokenUtil.createRefreshToken(key);

        log.debug("accessToken : "+accessToken);
        log.debug("refreshToken : "+refreshToken);

        tokenService.saveTokenInfo(authentication.getName(), accessToken, refreshToken);

        Cookie accessTokenCookie = new Cookie("ACCESS_TOKEN", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(ACCESS_TOKEN_EXPIRE_TIME);


        httpServletResponse.addCookie(accessTokenCookie);

    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String accessToken, HttpServletResponse httpServletResponse) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);

            JwtTokenUtil.isExpired(accessToken, key);

            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("AccessToken 이 만료되었습니다.");
            getNetAccessToken(accessToken, getAuthentication(accessToken), httpServletResponse);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
    }
        return false;
    }

    private void getNetAccessToken(String accessToken, Authentication authentication, HttpServletResponse httpServletResponse) {
        RefreshToken tokenInfo = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 토큰을 찾을 수 없습니다."));

        String refreshToken = tokenInfo.getRefreshToken();

        JwtTokenUtil.isExpired(refreshToken, key);

        Long userCid = Long.valueOf(tokenInfo.getId());
        UserEntity loggedInUser = userRepository.findById(userCid)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 유저가 없습니다"));

        generateTokenDto(authentication, httpServletResponse);
    }

    // 만료된 토큰이여도 정보를 꺼내기 위해 분리
    private Claims parseClaims(String accessToken) {
        try {
            return JwtTokenUtil.getClaims(accessToken, key);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}