package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseRuntimeException;

public class PostHaveNotLikedException extends BaseRuntimeException {
    public PostHaveNotLikedException() {
        super("You haven't liked this post.");
    }
}
