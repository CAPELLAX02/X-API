package com.x.backend.exceptions;

public class LikeNotFoundException extends NotFoundException {
    public LikeNotFoundException(Long id) {
        super("Like with ID: " + id + " not found.");
    }
}
