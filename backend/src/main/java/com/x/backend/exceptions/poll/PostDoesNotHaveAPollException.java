package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseNotFoundException;

public class PostDoesNotHaveAPollException extends BaseNotFoundException {
    public PostDoesNotHaveAPollException() {
        super("This post doesn't contain a poll.");
    }
}
