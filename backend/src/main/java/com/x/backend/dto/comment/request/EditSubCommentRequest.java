package com.x.backend.dto.comment.request;

import jakarta.validation.constraints.NotBlank;

public record EditSubCommentRequest(

        @NotBlank(message = "Comment content cannot be empty")
        String newContent

) {
}
