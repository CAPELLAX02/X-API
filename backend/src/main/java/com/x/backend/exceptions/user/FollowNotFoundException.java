package com.x.backend.exceptions.user;

import com.x.backend.exceptions.NotFoundException;

public class FollowNotFoundException extends NotFoundException {
    public FollowNotFoundException(Long id) {
        super("Follow with ID: " + id + " not found.");
    }
}
