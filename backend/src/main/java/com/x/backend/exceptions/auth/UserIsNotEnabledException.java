package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class UserIsNotEnabledException extends CustomRuntimeException {
    public UserIsNotEnabledException() {
        super("User is not enabled in the system. Please request a verification code.");
    }
}
