package com.x.backend.exceptions.user;

import com.x.backend.exceptions.NotFoundException;

public class UserNotFoundByPhoneException extends NotFoundException {
    public UserNotFoundByPhoneException(String phoneNumber) {
        super("User with phone number " + phoneNumber + " not found");
    }
}
