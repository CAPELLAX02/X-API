package com.x.backend.services.poll;

import com.x.backend.dto.poll.request.PollVoteRequest;
import com.x.backend.utils.api.BaseApiResponse;

public interface PostPollService {

    BaseApiResponse<String> voteInPoll(String username, Long postId, PollVoteRequest req);
    BaseApiResponse<String> revokePollVote(String username, Long pollId);

}
