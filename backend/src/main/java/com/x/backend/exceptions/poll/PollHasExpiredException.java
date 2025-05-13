package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.CustomRuntimeException;

public class PollHasExpiredException extends CustomRuntimeException {
    public PollHasExpiredException() {
        super("This poll has expired.");
    }
}
