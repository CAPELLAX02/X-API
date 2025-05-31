package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class RoleBaseNotFoundException extends BaseNotFoundException {
    public RoleBaseNotFoundException(String roleName) {
        super("Role '" + roleName + "' not found.");
    }
}
