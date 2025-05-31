package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseNotFoundException;

public class PollVoteBaseNotFoundException extends BaseNotFoundException {
    public PollVoteBaseNotFoundException() {
        super("Poll vote not found");
    }
}
