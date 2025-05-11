package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.NotFoundException;

public class PollVoteNotFoundException extends NotFoundException {
    public PollVoteNotFoundException() {
        super("Poll vote not found");
    }
}
