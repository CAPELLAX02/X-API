
package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.NotFoundException;

public class PollOptionNotFoundException extends NotFoundException {
    public PollOptionNotFoundException(Long id) {
        super("Poll option with ID: " + id + " not found.");
    }
}
