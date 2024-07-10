package com.ohmea.todayrecipe.exception;

public class AccessTokenExpiredException extends RuntimeException{
    public AccessTokenExpiredException(String message) {
        super(message);
    }
}
