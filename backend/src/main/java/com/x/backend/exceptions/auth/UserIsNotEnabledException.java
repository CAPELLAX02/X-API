package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class UserIsNotEnabledException extends BaseRuntimeException {
    public UserIsNotEnabledException() {
        super("User is not enabled in the system. Please request a verification code.");
    }
}
