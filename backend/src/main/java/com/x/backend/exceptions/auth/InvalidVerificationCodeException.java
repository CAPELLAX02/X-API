package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class InvalidVerificationCodeException extends CustomRuntimeException {
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}
