package com.x.backend.exceptions;

public class PostNotFoundException extends NotFoundException {
    public PostNotFoundException(Long id) {
        super("Post with ID: " + id + " not found.");
    }
}
