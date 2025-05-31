package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseConflictException;

public class PostAlreadyLikedException extends BaseConflictException {
    public PostAlreadyLikedException() {
        super("You have already liked this post.");
    }
}
