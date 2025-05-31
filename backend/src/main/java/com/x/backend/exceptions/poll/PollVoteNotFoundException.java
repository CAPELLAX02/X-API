package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseNotFoundException;

public class PollVoteNotFoundException extends BaseNotFoundException {
    public PollVoteNotFoundException() {
        super("Poll vote not found");
    }
}
