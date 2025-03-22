package com.x.backend.exceptions.auth;

public class UserIsNotEnabledException extends RuntimeException {
    public UserIsNotEnabledException() {
        super("User is not enabled in the system. Please request a verification code.");
    }
}
