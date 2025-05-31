package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseNotFoundException;

public class PostBaseNotFoundException extends BaseNotFoundException {
    public PostBaseNotFoundException(Long id) {
        super("Post with ID: " + id + " not found.");
    }
}
