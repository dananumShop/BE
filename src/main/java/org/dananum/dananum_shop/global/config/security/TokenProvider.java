package org.dananum.dananum_shop.global.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.dananum.dananum_shop.global.config.redis.entity.RefreshToken;
import org.dananum.dananum_shop.global.config.redis.repository.RefreshTokenRepository;
import org.dananum.dananum_shop.global.config.redis.service.TokenService;
import org.dananum.dananum_shop.global.util.JwtTokenUtil;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.global.web.advice.exception.jwt.CustomExpiredJwtException;
import org.dananum.dananum_shop.global.web.advice.exception.jwt.CustomJwtBadReqException;
import org.dananum.dananum_shop.global.web.advice.exception.jwt.CustomJwtException;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final String tokenType = "Bearer";

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

        tokenService.saveTokenInfo(authentication.getName(), refreshToken, accessToken);


        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, tokenType + " " + accessToken);
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

    public void validateToken(String accessToken, HttpServletResponse httpServletResponse) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);

            JwtTokenUtil.isExpired(accessToken, key);

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new CustomJwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("AccessToken 이 만료되었습니다.");
            throw new CustomExpiredJwtException("AccessToken 이 만료되었습니다.");
//            getNewAccessToken(accessToken, getAuthentication(accessToken), httpServletResponse);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new CustomJwtBadReqException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new CustomJwtBadReqException("JWT 토큰이 잘못되었습니다.");
    }
    }

    /**
     * 새로운 Access Token을 발급하는 메서드입니다.
     * HttpServletRequest에서 Access Token을 추출하고,
     * 추출한 Access Token을 사용하여 Refresh Token을 검증하고,
     * 유효한 경우 해당 유저의 Authentication 정보를 가져와서 새로운 Access Token을 생성합니다.
     * 생성된 Access Token과 함께 HttpServletResponse의 Authorization 헤더에 설정하여 반환합니다.
     *
     * @param httpServletRequest  현재 HTTP 요청 객체
     * @param httpServletResponse HTTP 응답 객체
     */
    public void getNewAccessToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        String accessToken = resolveToken(httpServletRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        RefreshToken tokenInfo = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 토큰을 찾을 수 없습니다."));

        String refreshToken = tokenInfo.getRefreshToken();

        JwtTokenUtil.isExpired(refreshToken, key);

        Long userCid = Long.valueOf(tokenInfo.getId());
        userRepository.findById(userCid)
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

    /**
     * HTTP 요청에서 Authorization 헤더에서 Access Token을 추출하는 메서드입니다.
     * Bearer 스키마를 사용하는 Authorization 헤더에서 Access Token을 추출하여 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return 추출된 Access Token 문자열, 만약 헤더에 Authorization이 없거나 Bearer 스키마가 아닌 경우 null을 반환합니다.
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}