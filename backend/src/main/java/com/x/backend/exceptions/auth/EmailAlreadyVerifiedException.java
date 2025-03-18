package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.AlreadyExistsException;

public class EmailAlreadyVerifiedException extends AlreadyExistsException {
    public EmailAlreadyVerifiedException(String email) {
        super("User with the email " + email + " already verified.");
    }
}
