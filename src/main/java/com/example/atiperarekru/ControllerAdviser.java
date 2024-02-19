package com.example.atiperarekru;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ControllerAdviser {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleNoDataFoundException() {
        return getGenericErrorResponse(INTERNAL_SERVER_ERROR, "An unexpected error has occurred");
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNoDataFoundException(NoDataFoundException e) {
        return getGenericErrorResponse(NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException e) {
        return getGenericErrorResponse(BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<Object> getGenericErrorResponse(HttpStatus status, String message) {
        List<String> body = List.of("status : " + status.value(), "message : " + message);
        return new ResponseEntity<>(body, status);
    }

}
