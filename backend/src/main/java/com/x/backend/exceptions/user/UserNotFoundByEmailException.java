package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class UserNotFoundByEmailException extends BaseNotFoundException {
    public UserNotFoundByEmailException(String email) {
        super("User with email '" + email + "' not found");
    }
}
