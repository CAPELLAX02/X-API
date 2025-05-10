package com.x.backend.exceptions.post;

public class PostHaveNotBookmarkedException extends RuntimeException {
    public PostHaveNotBookmarkedException() {
        super("You haven't bookmarked this post.");
    }
}
