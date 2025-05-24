package com.x.backend.dto.message.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendMessageRequest(

        @NotNull(message = "Recipient ID cannot be null.")
        Long recipientId,

        @NotBlank(message = "Message content cannot be blank.")
        String content,

        @Nullable
        Long replyToMessageId

) {
}
