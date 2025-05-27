package com.x.backend.services.message;

import com.x.backend.dto.message.request.SendMessageRequest;
import com.x.backend.dto.message.response.MessageResponse;

/**
 * Service interface for all message-related operations including
 * sending messages and marking them as read.
 */
public interface MessageService {

    /**
     * Sends a new message from the authenticated sender to a recipient.
     * Automatically creates a new conversation if none exists.
     *
     * @param senderUsername the username of the authenticated sender
     * @param req the message request payload, including recipient ID, content, and optional replyTo message ID
     * @return a {@link MessageResponse} containing metadata of the message sent
     */
    MessageResponse sendMessage(String senderUsername, SendMessageRequest req);

    /**
     * Marks the specified message as read by the recipient.
     * This should only be called by the message recipient.
     *
     * @param username the username of the authenticated user (must be the recipient)
     * @param messageId the ID of the message to mark as read
     */
    void markMessageAsRead(String username, Long messageId);

    /**
     * Marks the specified message as unread by the recipient.
     * This should only be called by the message recipient.
     *
     * @param username the username of the authenticated user (recipient)
     * @param messageId the ID of the message to be mark as unread
     */
    void undoMarkMessageAsRead(String username, Long messageId);

    /**
     * Edits the content of a previously sent message.
     * Only the original sender can perform this action.
     *
     * @param username the username of the authenticated sender
     * @param messageId the ID of the message to edit
     * @param newContent the new content to replace the old message content
     * @return the updated {@link MessageResponse} with new content
     */
    MessageResponse editMessage(String username, Long messageId, String newContent);

    /**
     * Soft-deletes the message for the sender, recipient, or both depending on context.
     * The actual message is not removed from the database.
     *
     * @param username the username of the user requesting deletion (sender or recipient)
     * @param messageId the ID of the message to delete
     */
    void deleteMessage(String username, Long messageId);

}
