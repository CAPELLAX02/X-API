package com.x.backend.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSubCommentRequest(

        @NotBlank(message = "Comment content cannot be blank")
        @Size(max = 500, message = "Comment content can be at most 500 characters")
        String content

) {
}
