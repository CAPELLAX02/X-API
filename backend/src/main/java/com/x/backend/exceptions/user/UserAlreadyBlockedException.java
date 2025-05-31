package com.x.backend.exceptions.user;

import com.x.backend.exceptions.AlreadyExistsException;

public class UserAlreadyBlockedException extends AlreadyExistsException {
    public UserAlreadyBlockedException() {
        super("You have already blocked this user.");
    }
}
