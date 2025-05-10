package com.x.backend.exceptions.post;

public class PostHaveNotLikedException extends RuntimeException {
    public PostHaveNotLikedException() {
        super("You haven't liked this post.");
    }
}
