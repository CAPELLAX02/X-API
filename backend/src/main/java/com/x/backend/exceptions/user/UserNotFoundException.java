package com.x.backend.exceptions.user;

import com.x.backend.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("User with ID: " + id + " not found.");
    }
}
