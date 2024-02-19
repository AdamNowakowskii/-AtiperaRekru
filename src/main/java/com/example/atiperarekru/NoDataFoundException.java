package com.example.atiperarekru;

public class NoDataFoundException extends Exception {

    public NoDataFoundException(String message) {
        super(message);
    }

    public static NoDataFoundException forDataNotFound() throws NoDataFoundException {
        throw new NoDataFoundException("No data found for given request");
    }

}
