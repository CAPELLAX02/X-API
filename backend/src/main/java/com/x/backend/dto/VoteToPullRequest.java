package com.x.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record VoteToPullRequest(

        @NotBlank(message = "Poll Choice ID cannot be blank")
        Integer pollChoiceId,

        @NotBlank(message = "User ID cannot be blank")
        Integer userId

) {
}
