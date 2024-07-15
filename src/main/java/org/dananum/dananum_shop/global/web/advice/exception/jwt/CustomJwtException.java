package org.dananum.dananum_shop.global.web.advice.exception.jwt;

public class CustomJwtException extends RuntimeException{

    public CustomJwtException(String message) {
        super(message);}
}
