package com.x.backend.dto.post.request;

import com.x.backend.models.enums.Audience;
import com.x.backend.models.enums.ReplyRestriction;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record CreatePostRequest(

        @NotBlank(message = "Post content cannot be blank.")
        @Size(max = 500, message = "Post content cannot exceed 500 characters.")
        String content,

        @NotNull(message = "Post audience must be specified (EVERYONE, FOLLOWERS_ONLY, MENTIONED_ONLY, PRIVATE).")
        Audience audience,

        @NotNull(message = "Reply restriction must be specified (EVERYONE, FOLLOWERS_ONLY, MENTIONED_ONLY, NO_ONE).")
        ReplyRestriction replyRestriction,

        @FutureOrPresent(message = "Scheduled date must be in the future or present.")
        LocalDateTime scheduledDate,

        @Nullable
        PollRequest poll

) {
        public record PollRequest(

                @Size(min = 2, max = 6, message = "Poll must have between 2 and 6 options.")
                List<@NotBlank(message = "Poll option cannot be blank.") @Size(max = 100) String> pollOptions,

                @Future(message = "Poll expiry date must be a future date.")
                LocalDateTime pollExpiryDate

        ) {}
}
