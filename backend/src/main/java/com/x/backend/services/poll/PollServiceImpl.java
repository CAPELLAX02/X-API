package com.x.backend.services.poll;

import com.x.backend.dto.VoteToPullRequest;
import com.x.backend.exceptions.InvalidPollEndDateException;
import com.x.backend.exceptions.PollChoiceNotFoundException;
import com.x.backend.exceptions.PollExpiredException;
import com.x.backend.exceptions.UserDoesNotExistException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Poll;
import com.x.backend.models.PollChoice;
import com.x.backend.repositories.PollChoiceRepository;
import com.x.backend.repositories.PollRepository;
import com.x.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PollServiceImpl implements PollService {

    private final PollRepository pollRepository;
    private final PollChoiceRepository pollChoiceRepository;
    private final UserRepository userRepository;

    public PollServiceImpl(
            PollRepository pollRepository,
            PollChoiceRepository pollChoiceRepository,
            UserRepository userRepository
    ) {
        this.pollRepository = pollRepository;
        this.pollChoiceRepository = pollChoiceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PollChoice generatePollChoice(PollChoice pollChoice) {
        return pollChoiceRepository.save(pollChoice);
    }

    @Override
    public Poll generatePoll(Poll poll) {
        if (poll.getEndDate().isBefore(LocalDateTime.now())) {
            throw new InvalidPollEndDateException();
        }
        return pollRepository.save(poll);
    }

    @Override
    public Poll voteToPoll(VoteToPullRequest voteToPullRequest) {
        Integer pollChoiceId = voteToPullRequest.pollChoiceId();
        Integer userId = voteToPullRequest.userId();

        PollChoice selectedPollChoice = pollChoiceRepository.findById(pollChoiceId)
                .orElseThrow(() -> new PollChoiceNotFoundException(pollChoiceId));

        Poll poll = selectedPollChoice.getPoll();

        if (poll.getEndDate().isBefore(LocalDateTime.now())) {
            throw new PollExpiredException();
        }

        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(UserDoesNotExistException::new);

        Optional<PollChoice> previousVote = poll.getPollChoices()
                .stream()
                .filter(choice -> choice.getVotes().contains(user))
                .findFirst();

        previousVote.ifPresent(pollChoice -> {
            pollChoice.getVotes().remove(user);
            pollChoiceRepository.save(pollChoice);
        });

        selectedPollChoice.getVotes().add(user);
        pollChoiceRepository.save(selectedPollChoice);

        return poll;
    }
}
