package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class InvalidVerificationCodeException extends BaseRuntimeException {
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}
