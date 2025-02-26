package com.x.backend.exceptions.post;

import com.x.backend.exceptions.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(Long id) {
        super("Comment with ID: " + id + " not found.");
    }
}
