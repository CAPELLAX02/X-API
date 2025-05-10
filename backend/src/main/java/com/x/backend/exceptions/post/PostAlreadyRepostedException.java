package com.x.backend.exceptions.post;

import com.x.backend.exceptions.AlreadyExistsException;

public class PostAlreadyRepostedException extends AlreadyExistsException {
    public PostAlreadyRepostedException() {
        super("You already reposted the post.");
    }
}
