package com.x.backend.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Integer id) {
        super("Post with id " + id + " not found");
    }
}
