package com.x.backend.dto.message.response;

import java.time.LocalDateTime;

public record ConversationResponse(

        Long conversationId,
        Long otherUserId,
        String otherUsername,
        String lastMessageSnippet,
        LocalDateTime lastMessageTime,
        int unreadMessageCount

) {
}
