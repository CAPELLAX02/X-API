package com.x.backend.repositories;

import com.x.backend.models.entities.PollVote;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PollVoteRepository extends BaseRepository<PollVote, Long> {

    boolean existsByUserIdAndPollId(Long userId, Long pollId);
    Optional<PollVote> findByPollIdAndUserId(Long pollId, Long userId);

    long countByPollId(Long pollId);
}
