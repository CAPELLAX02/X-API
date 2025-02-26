
package com.x.backend.exceptions;

public class PollOptionNotFoundException extends NotFoundException {
    public PollOptionNotFoundException(Long id) {
        super("Poll option with ID: " + id + " not found.");
    }
}
