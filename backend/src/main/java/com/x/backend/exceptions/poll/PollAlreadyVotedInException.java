package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseConflictException;

public class PollAlreadyVotedInException extends BaseConflictException {
    public PollAlreadyVotedInException() {
        super("You have already voted in this poll.");
    }
}
