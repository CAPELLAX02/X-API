package com.x.backend.services.poll;

import com.x.backend.dto.VoteToPullRequest;
import com.x.backend.models.Poll;
import com.x.backend.models.PollChoice;

public interface PollService {

    PollChoice generatePollChoice(PollChoice pollChoice);
    Poll generatePoll(Poll poll);
    Poll voteToPoll(VoteToPullRequest voteToPullRequest);

}
