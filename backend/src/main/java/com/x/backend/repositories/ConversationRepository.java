package com.x.backend.repositories;

import com.x.backend.models.entities.Conversation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends BaseRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.id = :userId ORDER BY c.lastMessageSentAt DESC")
    List<Conversation> findUserConversations(Long userId);

    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 WHERE p1.id = :userId1 AND p2.id = :userId2 AND c.isGroupChat = false")
    Optional<Conversation> findPrivateConversation(Long userId1, Long userId2);
}
