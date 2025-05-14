package com.x.backend.utils.builder;

import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.dto.comment.response.SubCommentResponse;
import com.x.backend.models.post.comment.Comment;
import com.x.backend.models.post.comment.SubComment;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class CommentResponseBuilder {

    private final SubCommentResponseBuilder subCommentResponseBuilder;

    public CommentResponseBuilder(SubCommentResponseBuilder subCommentResponseBuilder) {
        this.subCommentResponseBuilder = subCommentResponseBuilder;
    }

    public CommentResponse build(Comment comment) {
        return build(comment, null);
    }

    public CommentResponse build(Comment comment, Map<Long, List<SubComment>> subCommentsMap) {
        List<SubCommentResponse> subCommentsList = comment.getSubComments()
                .stream()
                .sorted(Comparator.comparing(SubComment::getCreatedAt))
                .map(subCommentResponseBuilder::build)
                .toList();

        return new CommentResponse(
                comment.getId(),
                comment.getAuthor().getUsername(),
                comment.getAuthor().getNickname(),
                comment.getContent(),
                comment.getLikes().size(),
                subCommentsList,
                comment.isDeleted(),
                comment.getCreatedAt(),
                comment.getEditedAt()
        );
    }

}
