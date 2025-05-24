package com.x.backend.repositories;

import com.x.backend.models.message.Conversation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends BaseRepository<Conversation, Long> {

    @Query("""
        SELECT c FROM Conversation c
        JOIN c.participants p1
        JOIN c.participants p2
        WHERE p1.id = :user1Id AND p2.id = :user2Id
        AND SIZE(c.participants) = 2
    """)
    Optional<Conversation> findDirectConversationBetween(Long user1Id, Long user2Id);
}
