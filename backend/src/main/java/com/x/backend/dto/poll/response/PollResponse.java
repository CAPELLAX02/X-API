package com.x.backend.dto.poll.response;

import java.time.LocalDateTime;
import java.util.List;

public record PollResponse(

        Long id,
        List<PollOptionResponse> options,
        LocalDateTime expiresAt,
        boolean hasVoted,
        Integer selectedOptionIndex

) {
}
