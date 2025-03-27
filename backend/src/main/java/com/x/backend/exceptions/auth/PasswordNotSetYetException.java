package com.x.backend.exceptions.auth;

public class PasswordNotSetYetException extends RuntimeException {
    public PasswordNotSetYetException() {
        super("User does not have a password yet.");
    }
}
