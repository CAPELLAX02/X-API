package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.NotFoundException;

public class EmailVerificationTokenNotFoundException extends NotFoundException {
    public EmailVerificationTokenNotFoundException() {
        super("Email verification token not found");
    }
}
