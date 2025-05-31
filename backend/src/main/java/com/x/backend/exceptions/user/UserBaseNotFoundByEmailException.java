package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class UserBaseNotFoundByEmailException extends BaseNotFoundException {
    public UserBaseNotFoundByEmailException(String email) {
        super("User with email '" + email + "' not found");
    }
}
