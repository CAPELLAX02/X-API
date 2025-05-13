package com.x.backend.utils.builder;

import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.models.entities.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CommentResponseBuilder {

    public CommentResponse buildCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getLikes().size(),
                comment.getCreatedAt(),
                List.of()
        );
    }

    public CommentResponse buildCommentResponse(Comment comment, Map<Long, List<Comment>> groupedReplies) {
        List<CommentResponse> replies = Optional.ofNullable(groupedReplies.get(comment.getId()))
                .orElse(List.of())
                .stream()
                .map(c -> buildCommentResponse(c, groupedReplies))
                .toList();

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getLikes().size(),
                comment.getCreatedAt(),
                replies
        );
    }

}
