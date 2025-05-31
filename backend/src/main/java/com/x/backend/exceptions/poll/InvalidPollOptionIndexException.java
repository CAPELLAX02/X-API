package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseRuntimeException;

public class InvalidPollOptionIndexException extends BaseRuntimeException {
    public InvalidPollOptionIndexException(int index) {
        super("Invalid poll option index: " + index);
    }
}
