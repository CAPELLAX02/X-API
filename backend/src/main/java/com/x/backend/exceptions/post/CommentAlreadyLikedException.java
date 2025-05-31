package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseConflictException;

public class CommentAlreadyLikedException extends BaseConflictException {
    public CommentAlreadyLikedException() {
        super("You have already liked this comment.");
    }
}
