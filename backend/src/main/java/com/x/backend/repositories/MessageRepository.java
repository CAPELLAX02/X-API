package com.x.backend.repositories;

import com.x.backend.models.entities.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends BaseRepository<Message, Long> {

    List<Message> findByConversationIdOrderBySentAtDesc(Long conversationId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.id = :conversationId AND m.user.id <> :userId AND m.isRead = false")
    long countUnreadMessages(Long conversationId, Long userId);

    List<Message> findByUserIdAndConversationIdOrderBySentAtDesc(Long userId, Long conversationId);
}
