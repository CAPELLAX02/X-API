package com.x.backend.exceptions.post;

import com.x.backend.exceptions.CustomRuntimeException;

public class CommentHaveNotLikedException extends CustomRuntimeException {
    public CommentHaveNotLikedException() {
        super("You haven't liked this comment.");
    }
}
