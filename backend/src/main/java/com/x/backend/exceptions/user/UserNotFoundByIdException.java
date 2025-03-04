package com.x.backend.exceptions.user;

import com.x.backend.exceptions.NotFoundException;

public class UserNotFoundByIdException extends NotFoundException {
    public UserNotFoundByIdException(Long id) {
        super("User with ID: " + id + " not found.");
    }
}
