package com.x.backend.exceptions.post;

import com.x.backend.exceptions.NotFoundException;

public class PostNotFoundException extends NotFoundException {
    public PostNotFoundException(Long id) {
        super("Post with ID: " + id + " not found.");
    }
}
