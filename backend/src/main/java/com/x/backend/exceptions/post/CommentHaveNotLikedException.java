package com.x.backend.exceptions.post;

import com.x.backend.exceptions.BaseRuntimeException;

public class CommentHaveNotLikedException extends BaseRuntimeException {
    public CommentHaveNotLikedException() {
        super("You haven't liked this comment.");
    }
}
