package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class UserBaseNotFoundByIdException extends BaseNotFoundException {
    public UserBaseNotFoundByIdException(Long id) {
        super("User with ID: " + id + " not found.");
    }
}
