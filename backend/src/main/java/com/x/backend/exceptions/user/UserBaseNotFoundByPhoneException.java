package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class UserBaseNotFoundByPhoneException extends BaseNotFoundException {
    public UserBaseNotFoundByPhoneException(String phoneNumber) {
        super("User with phone number " + phoneNumber + " not found");
    }
}
