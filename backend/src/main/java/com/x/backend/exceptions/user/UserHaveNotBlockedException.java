package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseRuntimeException;

public class UserHaveNotBlockedException extends BaseRuntimeException {
    public UserHaveNotBlockedException() {
        super("You haven't blocked this user.");
    }
}
