package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseConflictException;

public class UserAlreadyBlockedException extends BaseConflictException {
    public UserAlreadyBlockedException() {
        super("You have already blocked this user.");
    }
}
