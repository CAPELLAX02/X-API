
package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseNotFoundException;

public class PollOptionBaseNotFoundException extends BaseNotFoundException {
    public PollOptionBaseNotFoundException(Long id) {
        super("Poll option with ID: " + id + " not found.");
    }
}
