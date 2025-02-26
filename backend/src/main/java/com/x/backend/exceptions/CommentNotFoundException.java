package com.x.backend.exceptions;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(Long id) {
        super("Comment with ID: " + id + " not found.");
    }
}
