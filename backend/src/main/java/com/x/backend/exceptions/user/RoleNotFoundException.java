package com.x.backend.exceptions.user;

import com.x.backend.exceptions.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(String roleName) {
        super("Role \"" + roleName + "\" not found.");
    }
}
