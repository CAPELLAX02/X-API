package com.x.backend.exceptions.auth;

public class PasswordReusedException extends RuntimeException {
    public PasswordReusedException(String message) {
        super(message);
    }
}
