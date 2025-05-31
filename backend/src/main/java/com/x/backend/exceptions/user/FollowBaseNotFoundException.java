package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class FollowBaseNotFoundException extends BaseNotFoundException {
    public FollowBaseNotFoundException(Long id) {
        super("Follow with ID: " + id + " not found.");
    }
}
