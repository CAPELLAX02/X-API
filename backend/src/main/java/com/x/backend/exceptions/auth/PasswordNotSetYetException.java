package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class PasswordNotSetYetException extends BaseRuntimeException {
    public PasswordNotSetYetException() {
        super("User does not have a password yet.");
    }
}
