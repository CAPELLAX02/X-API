package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseNotFoundException;

public class PostDoesBaseNotHaveAPollException extends BaseNotFoundException {
    public PostDoesBaseNotHaveAPollException() {
        super("This post doesn't contain a poll.");
    }
}
