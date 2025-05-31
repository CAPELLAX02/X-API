package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class UserNotFoundByIdException extends BaseNotFoundException {
    public UserNotFoundByIdException(Long id) {
        super("User with ID: " + id + " not found.");
    }
}
