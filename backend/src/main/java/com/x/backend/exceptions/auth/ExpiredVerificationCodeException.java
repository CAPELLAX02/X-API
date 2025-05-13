package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class ExpiredVerificationCodeException extends CustomRuntimeException {
    public ExpiredVerificationCodeException(String message) {
        super(message);
    }
}
