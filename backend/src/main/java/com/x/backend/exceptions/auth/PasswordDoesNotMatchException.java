package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class PasswordDoesNotMatchException extends BaseRuntimeException {
    public PasswordDoesNotMatchException(String message) {
        super(message);
    }
}
