package com.x.backend.exceptions.auth;

public class ExpiredPasswordRecoveryCodeException extends RuntimeException {
    public ExpiredPasswordRecoveryCodeException(String message) {
        super(message);
    }
}
