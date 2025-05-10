package com.x.backend.exceptions.post;

import com.x.backend.exceptions.AlreadyExistsException;

public class PostHaveNotRepostedException extends AlreadyExistsException {
    public PostHaveNotRepostedException() {
        super("You haven't reposted the post.");
    }
}
