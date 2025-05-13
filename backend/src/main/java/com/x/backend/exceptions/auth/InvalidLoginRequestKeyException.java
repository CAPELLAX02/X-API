package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class InvalidLoginRequestKeyException extends CustomRuntimeException {
    public InvalidLoginRequestKeyException() {
        super("Invalid login request key. Available keys are: ['email', 'username', 'phone']");
    }
}
