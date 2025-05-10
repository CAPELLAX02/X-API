package com.x.backend.dto.comment.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(

        @NotBlank(message = "Comment content cannot be blank")
        @Size(max = 500, message = "Comment content can be at most 500 characters")
        String content,

        @Nullable
        Long parentCommentId // Optional

) {
}
