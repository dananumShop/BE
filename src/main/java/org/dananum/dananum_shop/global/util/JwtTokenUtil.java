package org.dananum.dananum_shop.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.global.config.redis.entity.RefreshToken;
import org.dananum.dananum_shop.global.config.redis.repository.RefreshTokenRepository;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;

import java.security.Key;
import java.util.Date;


@RequiredArgsConstructor
public class JwtTokenUtil {

    private static final String AUTHORITIES_KEY = "Authorization";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 1;  // 1일

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 기능 - 클레임 추출
     *
     * @param accessToken
     * @param key
     *
     * @return Claims
     */
    public static Claims getClaims(String accessToken, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }

    /**
     * 기능 - 토큰 만료 여부 확인
     *
     * @param token
     * @param key
     *
     * @return boolean
     */
    public static void isExpired(String token, Key key) {
        Claims claims = getClaims(token, key);
        Date expiration = claims.getExpiration();
        if(expiration != null && expiration.before(new Date())) {
            throw new ExpiredJwtException(null, claims, "토큰이 만료되었습니다.");
        }
    }

    /**
     * 기능 - 토큰에서 email 추출
     *
     * @param token
     * @param key
     *
     * @return user_email
     */
    public static String getEmail(String token, Key key) {
        Claims claims = getClaims(token, key);
        return claims.getSubject();
    }

    /**
     * 기능 - AccessToken 생성
     *
     * @param authorities
     * @param email
     * @param key
     *
     * @return accessToken
     */
    public static String createAccessToken(String authorities, String email, Key key) {
        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME)) // 30 만료
                .signWith(key)
                .compact();
    }

    /**
     * 기능 - RefreshToken 생성
     *
     * @param key
     *
     * @return refreshToken
     */
    public static String createRefreshToken(Key key) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME)) // 1일 만료
                .signWith(key)
                .compact();
    }

    public RefreshToken getInfo(String accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new CustomNotFoundException("일치하는 토큰을 찾을 수 없습니다."));
    }
}
