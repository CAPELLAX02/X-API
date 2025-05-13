package com.x.backend.exceptions.post;

import com.x.backend.exceptions.CustomRuntimeException;

public class PostHaveNotBookmarkedException extends CustomRuntimeException {
    public PostHaveNotBookmarkedException() {
        super("You haven't bookmarked this post.");
    }
}
