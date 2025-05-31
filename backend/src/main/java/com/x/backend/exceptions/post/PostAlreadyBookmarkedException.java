package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseConflictException;

public class PostAlreadyBookmarkedException extends BaseConflictException {
    public PostAlreadyBookmarkedException() {
        super("You have already bookmarked this post.");
    }
}
