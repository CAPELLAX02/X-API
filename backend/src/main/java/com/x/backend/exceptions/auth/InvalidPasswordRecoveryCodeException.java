package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class InvalidPasswordRecoveryCodeException extends CustomRuntimeException {
    public InvalidPasswordRecoveryCodeException(String message) {
        super(message);
    }
}
