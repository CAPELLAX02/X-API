package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class RoleNotFoundException extends BaseNotFoundException {
    public RoleNotFoundException(String roleName) {
        super("Role '" + roleName + "' not found.");
    }
}
