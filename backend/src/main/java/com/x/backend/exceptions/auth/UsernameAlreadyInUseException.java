package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseConflictException;

public class UsernameAlreadyInUseException extends BaseConflictException {
    public UsernameAlreadyInUseException() {
        super("Username already in use.");
    }
}
