package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseNotFoundException;

public class CommentNotFoundException extends BaseNotFoundException {
    public CommentNotFoundException(Long id) {
        super("Comment with ID: " + id + " not found.");
    }
}
