package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseNotFoundException;

public class CommentBaseNotFoundException extends BaseNotFoundException {
    public CommentBaseNotFoundException(Long id) {
        super("Comment with ID: " + id + " not found.");
    }
}
