package com.x.backend.exceptions;

public class PollChoiceNotFoundException extends RuntimeException {
    public PollChoiceNotFoundException(Integer pollChoiceId) {
        super("Poll choice with id " + pollChoiceId + " not found");
    }
}
