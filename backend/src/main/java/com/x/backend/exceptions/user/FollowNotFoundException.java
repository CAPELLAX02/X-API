package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class FollowNotFoundException extends BaseNotFoundException {
    public FollowNotFoundException(Long id) {
        super("Follow with ID: " + id + " not found.");
    }
}
