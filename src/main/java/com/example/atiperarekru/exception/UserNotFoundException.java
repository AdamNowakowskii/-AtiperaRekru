package com.example.atiperarekru.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("A user with given username doesn't exist");
    }

}
