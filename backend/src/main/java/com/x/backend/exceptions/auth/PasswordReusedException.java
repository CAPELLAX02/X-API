package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class PasswordReusedException extends CustomRuntimeException {
    public PasswordReusedException(String message) {
        super(message);
    }
}
