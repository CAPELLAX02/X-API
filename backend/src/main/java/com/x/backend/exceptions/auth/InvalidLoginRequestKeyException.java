package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class InvalidLoginRequestKeyException extends BaseRuntimeException {
    public InvalidLoginRequestKeyException() {
        super("Invalid login request key. Available keys are: ['email', 'username', 'phone']");
    }
}
