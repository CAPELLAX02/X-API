package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class ExpiredPasswordRecoveryCodeException extends CustomRuntimeException {
    public ExpiredPasswordRecoveryCodeException(String message) {
        super(message);
    }
}
