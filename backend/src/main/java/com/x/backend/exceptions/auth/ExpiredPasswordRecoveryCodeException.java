package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class ExpiredPasswordRecoveryCodeException extends BaseRuntimeException {
    public ExpiredPasswordRecoveryCodeException(String message) {
        super(message);
    }
}
