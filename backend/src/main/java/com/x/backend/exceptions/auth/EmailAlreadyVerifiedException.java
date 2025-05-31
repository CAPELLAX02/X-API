package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseConflictException;

public class EmailAlreadyVerifiedException extends BaseConflictException {
    public EmailAlreadyVerifiedException(String email) {
        super("User with the email " + email + " already verified.");
    }
}
