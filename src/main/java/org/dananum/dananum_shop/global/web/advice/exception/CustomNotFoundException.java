package org.dananum.dananum_shop.global.web.advice.exception;

public class CustomNotFoundException extends RuntimeException{

    CustomNotFoundException(){

    }

    public CustomNotFoundException(String message) {
        super(message);
    }

}
