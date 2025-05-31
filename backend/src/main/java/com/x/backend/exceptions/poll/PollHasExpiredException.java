package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseRuntimeException;

public class PollHasExpiredException extends BaseRuntimeException {
    public PollHasExpiredException() {
        super("This poll has expired.");
    }
}
