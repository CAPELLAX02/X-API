package com.x.backend.exceptions.post;

import com.x.backend.exceptions.CustomRuntimeException;

public class PostHaveNotLikedException extends CustomRuntimeException {
    public PostHaveNotLikedException() {
        super("You haven't liked this post.");
    }
}
