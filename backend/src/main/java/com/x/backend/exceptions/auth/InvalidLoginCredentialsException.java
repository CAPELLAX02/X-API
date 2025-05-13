package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class InvalidLoginCredentialsException extends CustomRuntimeException {
    public InvalidLoginCredentialsException() {
        super("Invalid login credentials");
    }
}
