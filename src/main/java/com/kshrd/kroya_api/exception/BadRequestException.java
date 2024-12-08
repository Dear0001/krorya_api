package com.kshrd.kroya_api.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message, BadCredentialsException e) {
        super(message);
    }
}
