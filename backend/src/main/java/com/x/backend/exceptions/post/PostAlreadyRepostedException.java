package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseConflictException;

public class PostAlreadyRepostedException extends BaseConflictException {
    public PostAlreadyRepostedException() {
        super("You already reposted the post.");
    }
}
