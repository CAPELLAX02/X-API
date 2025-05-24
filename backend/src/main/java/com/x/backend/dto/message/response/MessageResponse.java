package com.x.backend.dto.message.response;

import java.time.LocalDateTime;

public record MessageResponse(

        Long messageId,
        Long senderId,
        String senderUsername,
        Long recipientId,
        String content,
        boolean isRead,
        LocalDateTime sentAt

) {
}
