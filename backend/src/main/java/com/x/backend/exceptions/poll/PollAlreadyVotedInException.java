package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.AlreadyExistsException;

public class PollAlreadyVotedInException extends AlreadyExistsException {
    public PollAlreadyVotedInException() {
        super("You have already voted in this poll.");
    }
}
