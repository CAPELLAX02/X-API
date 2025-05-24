package com.x.backend.repositories;

import com.x.backend.models.message.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends BaseRepository<Message, Long> {

    @Query("""
        SELECT m FROM Message m
        WHERE m.conversation.id = :conversationId
          AND (m.sender.id = :userId OR m.recipient.id = :userId)
        ORDER BY m.sentAt DESC
    """)
    List<Message> findMessagesInConversationByUser(Long conversationId, Long userId);

    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    long countByConversationIdAndRecipientIdAndIsReadFalse(Long conversationId, Long recipientId);

    List<Message> findByRecipientIdAndIsReadFalse(Long recipientId);
}
