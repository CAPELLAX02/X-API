package com.x.backend.dto.poll.request;

import jakarta.validation.constraints.NotNull;

public record PollVoteRequest(

        @NotNull(message = "Poll option index number cannot be null")
        int selectedOptionIndex

) {
}
