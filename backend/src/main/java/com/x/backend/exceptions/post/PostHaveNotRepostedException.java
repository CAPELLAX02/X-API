package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseConflictException;

public class PostHaveNotRepostedException extends BaseConflictException {
    public PostHaveNotRepostedException() {
        super("You haven't reposted the post.");
    }
}
