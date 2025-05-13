package com.x.backend.repositories;

import com.x.backend.models.post.poll.Poll;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends BaseRepository<Poll, Long> {

    Optional<Poll> findByPostId(Long postId);

    List<Poll> findByExpiresAtAfterOrderByExpiresAtAsc(LocalDateTime timestamp);

    @Query("SELECT p FROM Poll p JOIN p.votes v GROUP BY p ORDER BY COUNT(v) DESC")
    List<Poll> findMostVotedPolls();
}
