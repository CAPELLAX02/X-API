package com.x.backend.utils.builder;

import com.x.backend.dto.post.request.PostInteractionContext;
import com.x.backend.dto.post.response.PostResponse;
import com.x.backend.models.entities.Image;
import com.x.backend.models.entities.Post;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostResponseBuilder {

    private final PollResponseBuilder pollResponseBuilder;

    public PostResponseBuilder(PollResponseBuilder pollResponseBuilder) {
        this.pollResponseBuilder = pollResponseBuilder;
    }

    public PostResponse build(Post p, PostInteractionContext ctx) {
        return new PostResponse(
                p.getId(),
                new PostResponse.AuthorInfo(
                        p.getAuthor().getUsername(),
                        p.getAuthor().getNickname(),
                        Optional.ofNullable(p.getAuthor().getProfilePicture())
                                .map(Image::getImageUrl)
                                .orElse(null)
                ),
                p.getContent(),
                p.getCreatedAt(),
                p.isReply(),
                Optional.ofNullable(p.getReplyTo()).map(Post::getId).orElse(null),
                new PostResponse.Engagement(
                        p.getLikes().size(),
                        p.getComments().size(),
                        p.getReposts().size(),
                        p.getBookmarks().size(),
                        p.getViews().size(),
                        ctx.liked(),
                        ctx.bookmarked()
                ),
                p.getMediaAttachments().stream()
                        .map(Image::getImageUrl)
                        .toList(),
                p.getAudience(),
                p.getReplyRestriction(),
                pollResponseBuilder.build(p.getPoll(), ctx)
        );
    }

}
