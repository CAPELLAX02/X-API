package com.x.backend.dto.post.response;

import com.x.backend.dto.poll.response.PollResponse;
import com.x.backend.models.post.enums.Audience;
import com.x.backend.models.post.enums.ReplyRestriction;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        AuthorInfo author,
        String content,
        LocalDateTime createdAt,
        boolean isReply,
        Long replyToPostId,
        Engagement engagement,
        List<String> mediaUrls,
        Audience audience,
        ReplyRestriction replyRestriction,
        PollResponse poll
) {
    public record AuthorInfo(
            String username,
            String nickname,
            String profileImageUrl
    ) {
    }
    public record Engagement(
            int likeCount,
            int commentCount,
            int repostCount,
            int bookmarkCount,
            int viewCount,
            boolean likedByCurrentUser,
            boolean bookmarkedByCurrentUser
    ) {
    }
}
