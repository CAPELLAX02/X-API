package com.x.backend.exceptions.post;

import com.x.backend.exceptions.AlreadyExistsException;

public class PostAlreadyLikedException extends AlreadyExistsException {
    public PostAlreadyLikedException() {
        super("You have already liked this post.");
    }
}
