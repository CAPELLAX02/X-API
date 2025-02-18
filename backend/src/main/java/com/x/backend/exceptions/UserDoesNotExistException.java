package com.x.backend.exceptions;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException() {
        super("The user you are looking for does not exist");
    }
}
