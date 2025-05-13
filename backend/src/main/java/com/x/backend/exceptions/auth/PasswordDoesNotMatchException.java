package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class PasswordDoesNotMatchException extends CustomRuntimeException {
    public PasswordDoesNotMatchException(String message) {
        super(message);
    }
}
