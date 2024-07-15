package org.dananum.dananum_shop.global.web.advice.exception.jwt;

public class CustomExpiredJwtException extends RuntimeException {

    public CustomExpiredJwtException(String message) {
        super(message);
    }
}
