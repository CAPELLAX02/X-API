package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseNotFoundException;

public class EmailVerificationTokenBaseNotFoundException extends BaseNotFoundException {
    public EmailVerificationTokenBaseNotFoundException() {
        super("Email verification token not found");
    }
}
