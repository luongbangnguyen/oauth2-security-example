package com.example.oauth2.client.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_IS_ANONYMOUS(HttpStatus.UNAUTHORIZED, "current user is anonymous");

    private final String description;
    private final HttpStatus errorStatus;
    ErrorCode(HttpStatus errorStatus, String description) {
        this.errorStatus = errorStatus;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getErrorStatus() {
        return errorStatus;
    }
}
