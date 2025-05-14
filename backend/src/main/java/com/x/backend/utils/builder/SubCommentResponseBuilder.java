package com.x.backend.utils.builder;

import com.x.backend.dto.comment.response.SubCommentResponse;
import com.x.backend.models.post.comment.SubComment;
import org.springframework.stereotype.Component;

@Component
public class SubCommentResponseBuilder {

    public SubCommentResponse build(SubComment sc) {
        return new SubCommentResponse(
                sc.getId(),
                sc.getAuthor().getUsername(),
                sc.getAuthor().getNickname(),
                sc.getContent(),
                sc.getLikes().size(),
                sc.isDeleted(),
                sc.getCreatedAt(),
                sc.getEditedAt()
        );
    }

}
