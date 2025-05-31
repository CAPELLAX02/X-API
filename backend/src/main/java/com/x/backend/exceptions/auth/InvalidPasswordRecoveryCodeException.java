package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class InvalidPasswordRecoveryCodeException extends BaseRuntimeException {
    public InvalidPasswordRecoveryCodeException(String message) {
        super(message);
    }
}
