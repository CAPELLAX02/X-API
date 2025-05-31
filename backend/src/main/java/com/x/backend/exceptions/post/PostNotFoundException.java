package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseNotFoundException;

public class PostNotFoundException extends BaseNotFoundException {
    public PostNotFoundException(Long id) {
        super("Post with ID: " + id + " not found.");
    }
}
