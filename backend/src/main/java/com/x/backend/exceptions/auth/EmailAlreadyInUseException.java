package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseConflictException;

public class EmailAlreadyInUseException extends BaseConflictException {
    public EmailAlreadyInUseException(String email) {
        super("Email '" + email + "' already in use");
    }
}
