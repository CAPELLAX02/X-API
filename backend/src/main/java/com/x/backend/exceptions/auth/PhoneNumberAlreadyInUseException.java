package com.x.backend.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneNumberAlreadyInUseException extends RuntimeException {
    public PhoneNumberAlreadyInUseException(String phone) {
        super("Phone number \"" + phone + "\" already in use");
    }
}
