
package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseNotFoundException;

public class PollOptionNotFoundException extends BaseNotFoundException {
    public PollOptionNotFoundException(Long id) {
        super("Poll option with ID: " + id + " not found.");
    }
}
