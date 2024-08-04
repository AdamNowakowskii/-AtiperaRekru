package com.example.atiperarekru.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdviser {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleInternalServerError() {
//        return ErrorResponse.internalServerError();
//    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ErrorResponse.of(NOT_FOUND.value(), e.getMessage());
    }

}
