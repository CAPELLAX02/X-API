package com.x.backend.exceptions.post;

import com.x.backend.exceptions.NotFoundException;

public class LikeNotFoundException extends NotFoundException {
    public LikeNotFoundException(Long id) {
        super("Like with ID: " + id + " not found.");
    }
}
