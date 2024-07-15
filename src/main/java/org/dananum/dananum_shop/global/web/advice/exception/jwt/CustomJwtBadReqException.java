package org.dananum.dananum_shop.global.web.advice.exception.jwt;

public class CustomJwtBadReqException extends RuntimeException {

    public CustomJwtBadReqException(String message) {
        super(message);
    }
}
