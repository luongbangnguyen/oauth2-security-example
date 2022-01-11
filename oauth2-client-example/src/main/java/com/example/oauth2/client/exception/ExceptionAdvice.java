package com.example.oauth2.client.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity<ErrorCodeResponse> errorCodeException(ErrorCodeException ex) {
        ErrorCodeResponse errorCodeResponse = new ErrorCodeResponse();
        errorCodeResponse.errorCode = ex.getErrorCode();
        errorCodeResponse.description = ex.getDescription();
        return ResponseEntity.status(ex.getErrorCode().getErrorStatus()).body(errorCodeResponse);
    }

}
