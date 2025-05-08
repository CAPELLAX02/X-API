package com.x.backend.exceptions.post;

public class HaveNotLikedException extends RuntimeException {
    public HaveNotLikedException() {
        super("You have not liked this post yet.");
    }
}
