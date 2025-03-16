package com.x.backend.exceptions.auth;

public class InvalidPasswordRecoveryCodeException extends RuntimeException {
    public InvalidPasswordRecoveryCodeException(String message) {
        super(message);
    }
}
