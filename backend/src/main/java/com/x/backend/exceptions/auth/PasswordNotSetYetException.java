package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class PasswordNotSetYetException extends CustomRuntimeException {
    public PasswordNotSetYetException() {
        super("User does not have a password yet.");
    }
}
