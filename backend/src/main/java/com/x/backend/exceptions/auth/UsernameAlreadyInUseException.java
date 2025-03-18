package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.AlreadyExistsException;

public class UsernameAlreadyInUseException extends AlreadyExistsException {
    public UsernameAlreadyInUseException() {
        super("Username already in use.");
    }
}
