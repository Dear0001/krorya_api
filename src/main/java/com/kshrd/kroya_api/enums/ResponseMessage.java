package com.kshrd.kroya_api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage implements IResponseMessage {
    OK("200", "OK"),
    INCORRECT_USERNAME("001", "Incorrect phone number!"),
    INCORRECT_PASSWORD("002", "Incorrect password!"),
    FORBIDDEN("403", "Forbidden"),
    UNAUTHORIZED("401", "UNAUTHORIZED"),
    BADREQUEST("004", "BADREQUEST"),
    INVALID_TOKEN_SIGNATURE("0004", "Invalid token signature"),
    INVALID_TOKEN("005", "Invalid token"),
    TOKEN_EXPIRED("006", "Token expired"),
    UNSUPPORTED_TOKEN("007", "Unsupported token"),
    NOTAUTHORIZED("003", "NOT YET AUTHORIZED!"),
    CHECKPHONENUMBER("009", "PHONE NUMBER HAVE ALREADY"),
    PASSWORD_NOT_MATCH("010", "PASSWORD NOT MATCH");
    private final String code;
    private final String message;
}
