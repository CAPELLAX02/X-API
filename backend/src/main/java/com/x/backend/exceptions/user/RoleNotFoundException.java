package com.x.backend.exceptions.user;

import com.x.backend.exceptions.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(Long id) {
        super("Role with ID: " + id + " not found.");
    }
}
