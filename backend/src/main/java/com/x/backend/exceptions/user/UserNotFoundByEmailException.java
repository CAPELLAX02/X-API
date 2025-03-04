package com.x.backend.exceptions.user;

import com.x.backend.exceptions.NotFoundException;

public class UserNotFoundByEmailException extends NotFoundException {
    public UserNotFoundByEmailException(String email) {
        super("User with email '" + email + "' not found");
    }
}
