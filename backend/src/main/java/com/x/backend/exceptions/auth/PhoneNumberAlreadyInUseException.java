package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.AlreadyExistsException;

public class PhoneNumberAlreadyInUseException extends AlreadyExistsException {
    public PhoneNumberAlreadyInUseException(String phone) {
        super("Phone number '" + phone + "' already in use");
    }
}
