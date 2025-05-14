package com.x.backend.dto.comment.response;

import java.time.LocalDateTime;

public record SubCommentResponse(
        Long id,
        String authorUsername,
        String authorNickname,
        String content,
        int likeCount,
        boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime editedAt
) {
}
