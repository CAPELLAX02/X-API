package com.x.backend.services.poll;

import com.x.backend.dto.poll.request.PollVoteRequest;
import com.x.backend.exceptions.poll.*;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.post.poll.Poll;
import com.x.backend.models.post.poll.PollOption;
import com.x.backend.models.post.poll.PollVote;
import com.x.backend.models.post.Post;
import com.x.backend.repositories.PollRepository;
import com.x.backend.repositories.PollVoteRepository;
import com.x.backend.repositories.PostRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostPollServiceImpl implements PostPollService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final PollVoteRepository pollVoteRepository;
    private final PollRepository pollRepository;

    public PostPollServiceImpl(final UserService userService,
                               final PostRepository postRepository,
                               final PollVoteRepository pollVoteRepository,
                               final PollRepository pollRepository
    ) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.pollVoteRepository = pollVoteRepository;
        this.pollRepository = pollRepository;
    }

    @Override
    public BaseApiResponse<String> voteInPoll(String username, Long postId, PollVoteRequest req) {
        ApplicationUser user = userService.getUserByUsername(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        Poll poll = post.getPoll();

        if (poll == null)
            throw new PostDoesNotHaveAPollException();

        if (poll.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new PollHasExpiredException();

        if (pollVoteRepository.existsByUserIdAndPollId(user.getId(), poll.getId()))
            throw new PollAlreadyVotedInException();

        List<PollOption> pollOptions = poll.getOptions();

        if (req.selectedOptionIndex() < 0 || req.selectedOptionIndex() >= pollOptions.size())
            throw new InvalidPollOptionIndexException(req.selectedOptionIndex());

        PollOption selectedOption = pollOptions.get(req.selectedOptionIndex());

        PollVote vote = new PollVote();
        vote.setPoll(poll);
        vote.setOption(selectedOption);
        vote.setUser(user);
        pollVoteRepository.save(vote);

        return BaseApiResponse.success("Poll vote recorded successfully.");
    }

    // TODO: Think about which is more convenient in "voteInPoll" and "revokePollVote" methods:
    //              BaseApiResponse<String>    OR    BaseApiResponse<PostResponse>

    @Override
    public BaseApiResponse<String> revokePollVote(String username, Long pollId) {
        ApplicationUser user = userService.getUserByUsername(username);
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new PollNotFoundException(pollId));

        if (poll.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new PollHasExpiredException();

        PollVote existingVote = pollVoteRepository.findByPollIdAndUserId(pollId, user.getId()).orElseThrow(PollVoteNotFoundException::new);
        pollVoteRepository.delete(existingVote);

        return BaseApiResponse.success("Poll vote revoked successfully.");
    }

}
