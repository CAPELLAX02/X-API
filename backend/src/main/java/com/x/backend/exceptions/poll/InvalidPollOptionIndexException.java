package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.CustomRuntimeException;

public class InvalidPollOptionIndexException extends CustomRuntimeException {
    public InvalidPollOptionIndexException(int index) {
        super("Invalid poll option index: " + index);
    }
}
