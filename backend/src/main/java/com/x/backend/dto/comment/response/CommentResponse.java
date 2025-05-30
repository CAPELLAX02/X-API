package com.x.backend.dto.comment.response;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        Long id,
        String authorUsername,
        String authorNickname,
        String content,
        int likeCount,
        List<SubCommentResponse> subComments,
        boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime editedAt
) {
}
