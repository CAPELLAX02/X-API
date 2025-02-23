package com.x.backend.dto;

import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Poll;
import com.x.backend.models.Post;
import com.x.backend.models.enums.Audience;
import com.x.backend.models.enums.ReplyRestriction;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record CreatePostRequest(

        @NotBlank(message = "Post content cannot be blank")
        String content,

        @NotNull(message = "Author of the post must be provided")
        ApplicationUser author,

        @Nullable
        Set<Post> replies,

        @Nullable
        Boolean scheduled,

        @Nullable
        LocalDateTime scheduledDate,

        @NotNull(message = "Audience of the post must be provided")
        Audience audience,

        @NotNull(message = "Reply restriction must be provided")
        ReplyRestriction replyRestriction,

        @Nullable
        Poll poll

) {
}
