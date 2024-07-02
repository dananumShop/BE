package org.dananum.dananum_shop.global.web.advice.exception;

public class CustomAccessDeniedException extends RuntimeException{

    public CustomAccessDeniedException(String message){
        super(message);
    }
}
