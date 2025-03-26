package com.x.backend.dto.post.response;

public record PollOptionResponse(

        int index,
        String text,
        int voteCount

) {
}
