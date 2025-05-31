package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class PasswordReusedException extends BaseRuntimeException {
    public PasswordReusedException(String message) {
        super(message);
    }
}
