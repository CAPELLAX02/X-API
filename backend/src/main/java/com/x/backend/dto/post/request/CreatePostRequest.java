package com.x.backend.dto.post.request;

import com.x.backend.models.enums.Audience;
import com.x.backend.models.enums.ReplyRestriction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record CreatePostRequest(

        @NotBlank(message = "Post content cannot be blank.")
        @Size(max = 500, message = "Post content cannot exceed 500 characters.")
        String content,

        Audience audience,
        ReplyRestriction replyRestriction,

        LocalDateTime scheduledDate,

        boolean hasPoll,
        List<String> pollOptions,
        LocalDateTime pollExpiryDate

) {
}
