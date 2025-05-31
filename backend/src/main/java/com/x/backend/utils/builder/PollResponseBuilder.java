package com.x.backend.utils.builder;

import com.x.backend.dto.post.request.PostInteractionContext;
import com.x.backend.dto.poll.response.PollOptionResponse;
import com.x.backend.dto.poll.response.PollResponse;
import com.x.backend.models.post.poll.Poll;
import com.x.backend.models.post.poll.PollVote;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PollResponseBuilder {

    public PollResponse build(Poll poll, PostInteractionContext ctx) {
        if (poll == null) return null;

        List<PollVote> votes = poll.getVotes();

        List<PollOptionResponse> options = poll.getOptions()
                .stream()
                .map(opt -> {
                    long voteCount = votes.stream()
                            .filter(v -> v.getOption().equals(opt))
                            .count();
                    int optionIndex = poll.getOptions().indexOf(opt);
                    return new PollOptionResponse(optionIndex,  opt.getOptionText(), (int) voteCount);
                })
                .toList();

        return new PollResponse(
                poll.getId(),
                options,
                poll.getExpiresAt(),
                ctx.hasVotedInPoll(),
                ctx.selectedOptionIndex()
        );
    }

}
