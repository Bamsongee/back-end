package com.ohmea.todayrecipe.exception;

public class IngredientNotFoundException extends RuntimeException{
    public IngredientNotFoundException(String msg) {
        super(msg);
    }
}
