package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.AlreadyExistsException;

public class EmailAlreadyInUseException extends AlreadyExistsException {
    public EmailAlreadyInUseException(String email) {
        super("Email '" + email + "' already in use");
    }
}
