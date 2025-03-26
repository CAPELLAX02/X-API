package com.x.backend.dto.post.response;

import com.x.backend.models.enums.Audience;
import com.x.backend.models.enums.ReplyRestriction;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(

        Long id,
        String authorUsername,
        String authorNickname,
        String authorProfileImageUrl,

        String content,
        LocalDateTime createdAt,
        boolean isReply,
        Long replyToPostId,

        int likeCount,
        int commentCount,
        int repostCount,
        int bookmarkCount,
        int viewCount,
        boolean likedByCurrentUser,
        boolean bookmarkedByCurrentUser,

        List<String> mediaUrls,

        Audience audience,
        ReplyRestriction replyRestriction,

        PollResponse poll // nullable

) {
}
