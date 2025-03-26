package com.x.backend.dto.post.response;

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
