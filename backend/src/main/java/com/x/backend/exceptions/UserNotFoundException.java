package com.x.backend.exceptions;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("User with ID: " + id + " not found.");
    }
}
