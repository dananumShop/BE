package org.dananum.dananum_shop.global.web.advice.exception;

public class CustomNoSuchElementException extends RuntimeException{
    public CustomNoSuchElementException(String message){
        super(message);
    }
}
