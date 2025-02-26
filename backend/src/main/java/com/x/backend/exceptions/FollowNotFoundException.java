package com.x.backend.exceptions;

public class FollowNotFoundException extends NotFoundException {
    public FollowNotFoundException(Long id) {
        super("Follow with ID: " + id + " not found.");
    }
}
