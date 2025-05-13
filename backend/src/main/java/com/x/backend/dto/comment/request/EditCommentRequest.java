package com.x.backend.dto.comment.request;

import jakarta.validation.constraints.NotBlank;

public record EditCommentRequest(

        @NotBlank(message = "New comment cannot be empty.")
        String newContent

) {
}
