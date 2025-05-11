package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.NotFoundException;

public class PostDoesNotHaveAPollException extends NotFoundException {
    public PostDoesNotHaveAPollException() {
        super("This post doesn't contain a poll.");
    }
}
