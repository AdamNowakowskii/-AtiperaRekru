package com.example.atiperarekru.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.ResponseEntity;

public record ErrorResponse(int status, String message) {

    public static ResponseEntity<ErrorResponse> of(int status, String message) {
        ErrorResponse response = new ErrorResponse(status, message);
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ErrorResponse> internalServerError() {
        ErrorResponse response = new ErrorResponse(500, "An unexpected error has occurred");
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

}
