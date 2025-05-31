package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseConflictException;

public class PhoneNumberAlreadyInUseException extends BaseConflictException {
    public PhoneNumberAlreadyInUseException(String phone) {
        super("Phone number '" + phone + "' already in use");
    }
}
