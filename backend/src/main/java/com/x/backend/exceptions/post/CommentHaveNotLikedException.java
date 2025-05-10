package com.x.backend.exceptions.post;

public class CommentHaveNotLikedException extends RuntimeException {
    public CommentHaveNotLikedException() {
        super("You haven't liked this comment.");
    }
}
