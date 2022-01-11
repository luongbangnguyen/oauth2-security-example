package com.example.oauth2.server.exception;

public enum ErrorCode {

    USER_HAS_BEEN_REGISTERED("User has been registered"),
    INVALID_TOKEN("Token is invalid"),
    AUDIENCE_NOT_MATCHING("Audience is not matching"),
    TOKEN_EXPIRED("Token is expired"),
    GRANT_TYPE_NOT_SUPPORT("Grant type is not support"),
    CLIENT_ID_INVALID("client id or client password invalid");

    ErrorCode(String description) {
        this.description = description;
    }

    private final String description;

    public String getDescription() {
        return description;
    }
}
