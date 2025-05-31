package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class UserNotFoundByPhoneException extends BaseNotFoundException {
    public UserNotFoundByPhoneException(String phoneNumber) {
        super("User with phone number " + phoneNumber + " not found");
    }
}
