package com.x.backend.dto.comment.response;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        Long id,
        String content,
        String authorUsername,
        Long parentCommentId,
        Integer likes,
        LocalDateTime createdAt,
        List<CommentResponse> replies
) {
}
