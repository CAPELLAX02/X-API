package com.x.backend.exceptions;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(Long id) {
        super("Role with ID: " + id + " not found.");
    }
}
