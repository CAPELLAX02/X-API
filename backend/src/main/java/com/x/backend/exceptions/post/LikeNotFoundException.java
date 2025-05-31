package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseNotFoundException;

public class LikeNotFoundException extends BaseNotFoundException {
    public LikeNotFoundException(Long id) {
        super("Like with ID: " + id + " not found.");
    }
}
