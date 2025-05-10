package com.x.backend.exceptions.post;

import com.x.backend.exceptions.AlreadyExistsException;

public class CommentAlreadyLikedException extends AlreadyExistsException {
    public CommentAlreadyLikedException() {
        super("You have already liked this comment.");
    }
}
