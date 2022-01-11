package com.example.oauth2.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse entityNotFoundException(EntityNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.errCode = "400";
        response.errMessage = e.getMessage();
        return response;
    }

    @ExceptionHandler(ErrorCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse errorCodeException(ErrorCodeException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.errCode = e.getErrorCode().name();
        response.errMessage = e.getDescription();
        return response;
    }

}
