package com.x.backend.dto.poll.response;

public record PollOptionResponse(

        int index,
        String text,
        int voteCount

) {
}
