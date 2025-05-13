package com.x.backend.dto.post.request;

public record PostInteractionContext(
        boolean liked,
        boolean bookmarked,
        boolean hasVotedInPoll,
        int selectedOptionIndex
) {
}
