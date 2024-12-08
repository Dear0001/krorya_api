package com.kshrd.kroya_api.exception;

public class InvalidValueExceptionHandler extends RuntimeException {
    public InvalidValueExceptionHandler(String messsage) {
        super(messsage);
    }
}
