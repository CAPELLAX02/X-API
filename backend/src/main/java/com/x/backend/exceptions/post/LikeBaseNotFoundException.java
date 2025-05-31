package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseNotFoundException;

public class LikeBaseNotFoundException extends BaseNotFoundException {
    public LikeBaseNotFoundException(Long id) {
        super("Like with ID: " + id + " not found.");
    }
}
