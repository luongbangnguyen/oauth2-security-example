package com.example.oauth2.client.exception;

public class ErrorCodeException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String description;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }

    public ErrorCodeException(ErrorCode errorCode, String description) {
        super(description);
        this.errorCode = errorCode;
        this.description = description;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }
}
