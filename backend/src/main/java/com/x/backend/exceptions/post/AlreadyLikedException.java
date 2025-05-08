package com.x.backend.exceptions.post;

import com.x.backend.exceptions.AlreadyExistsException;

public class AlreadyLikedException extends AlreadyExistsException {
    public AlreadyLikedException() {
        super("You have already liked this post.");
    }
}
