package com.x.backend.repositories;

import com.x.backend.models.entities.PollVote;
import org.springframework.stereotype.Repository;

@Repository
public interface PollVoteRepository extends BaseRepository<PollVote, Long> {

    boolean existsByUserIdAndPollId(Long userId, Long pollId);

    long countByPollId(Long pollId);
}
