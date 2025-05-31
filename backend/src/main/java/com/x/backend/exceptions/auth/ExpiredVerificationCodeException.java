package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class ExpiredVerificationCodeException extends BaseRuntimeException {
    public ExpiredVerificationCodeException(String message) {
        super(message);
    }
}
