package com.x.backend.exceptions.post;

import com.x.backend.exceptions.AlreadyExistsException;

public class PostAlreadyBookmarkedException extends AlreadyExistsException {
    public PostAlreadyBookmarkedException() {
        super("You have already bookmarked this post.");
    }
}
