package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseRuntimeException;

public class PostHaveNotBookmarkedException extends BaseRuntimeException {
    public PostHaveNotBookmarkedException() {
        super("You haven't bookmarked this post.");
    }
}
