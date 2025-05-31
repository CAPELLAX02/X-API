package com.x.backend.services.message;

import com.x.backend.dto.message.request.SendMessageRequest;
import com.x.backend.dto.message.response.MessageResponse;
import com.x.backend.exceptions.message.MessageAlreadyMarkedAsReadException;
import com.x.backend.exceptions.message.MessageHaveNotMarkedAsReadException;
import com.x.backend.exceptions.message.MessageNotFoundException;
import com.x.backend.exceptions.user.UserNotFoundByIdException;
import com.x.backend.models.message.Conversation;
import com.x.backend.models.message.Message;
import com.x.backend.models.message.MessageRead;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.repositories.ConversationRepository;
import com.x.backend.repositories.MessageReadRepository;
import com.x.backend.repositories.MessageRepository;
import com.x.backend.utils.builder.MessageResponseBuilder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private final ApplicationUserRepository applicationUserRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessageReadRepository messageReadRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageResponseBuilder messageResponseBuilder;

    public MessageServiceImpl(final ApplicationUserRepository applicationUserRepository,
                              final ConversationRepository conversationRepository,
                              final MessageRepository messageRepository,
                              final MessageReadRepository messageReadRepository,
                              final SimpMessagingTemplate messagingTemplate,
                              final MessageResponseBuilder messageResponseBuilder
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.messageReadRepository = messageReadRepository;
        this.messagingTemplate = messagingTemplate;
        this.messageResponseBuilder = messageResponseBuilder;
    }

    private Message getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException(messageId));
    }

    private ApplicationUser getUserById(Long userId) {
        return applicationUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundByIdException(userId));
    }

    private ApplicationUser getUserByUsername(String username) {
        return applicationUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public MessageResponse sendMessage(String senderUsername, SendMessageRequest req) {
        ApplicationUser sender = getUserByUsername(senderUsername);
        ApplicationUser recipient = getUserById(req.recipientId());

        // Find or create conversation between participants
        Conversation conversation = conversationRepository.findDirectConversationBetween(sender.getId(), recipient.getId())
                .orElseGet(() -> {
                    Conversation conv = new Conversation();
                    conv.setParticipants(new HashSet<>(Set.of(sender, recipient)));
//                    conv.setGroupChat(false);
//                    conv.setGroupName(null);
                    // TODO: Handle the group chat conversation as a distinct logic
                    conv.setCreatedAt(LocalDateTime.now());
                    conv.setLastMessageSentAt(LocalDateTime.now());
                    return conversationRepository.save(conv);
                });

        // Optional replyTo
        Message replyTo = null;
        if (req.replyToMessageId() != null) {
            replyTo = messageRepository.findById(req.replyToMessageId())
                    .orElseThrow(() -> new MessageNotFoundException(req.replyToMessageId()));
        }

        // Create message
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(req.content());
        message.setReplyTo(replyTo);
        message.setSentAt(LocalDateTime.now());

        message.setRead(false);
        message.setDeletedForSender(false);
        message.setDeletedForReceiver(false);
//        message.setMediaAttachment(null);
        // TODO: Handle the medias in messages later on.

        messageRepository.save(message);

        conversation.setLastMessageSentAt(message.getSentAt());
        conversationRepository.save(conversation);

        MessageResponse messageResponse = messageResponseBuilder.build(message);

        messagingTemplate.convertAndSendToUser(
                recipient.getUsername(),
                "/queue/messages",
                messageResponse
        );

        return messageResponse;
    }

    @Override
    public void markMessageAsRead(String username, Long messageId) {
        ApplicationUser user = getUserByUsername(username);
        Message message = getMessageById(messageId);

        if (!message.getRecipient().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only the recipient can mark this message as read.");
        }

        if (message.isRead()) {
            throw new MessageAlreadyMarkedAsReadException();
        }

        message.setRead(true);
        MessageRead readMessage = new MessageRead();
        readMessage.setUser(user);
        readMessage.setMessage(message);
        readMessage.setReadAt(LocalDateTime.now());
        messageReadRepository.save(readMessage);
    }

    @Override
    public void undoMarkMessageAsRead(String username, Long messageId) {
        Message message = getMessageById(messageId);
        ApplicationUser user = getUserByUsername(username);

        if (!message.getRecipient().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only the recipient can undo the read status of this message.");
        }

        if (!message.isRead()) {
            throw new MessageHaveNotMarkedAsReadException();
        }

        messageReadRepository.findByUserIdAndMessage(user.getId(), message)
                .ifPresent(messageReadRepository::delete);

        message.setRead(false);
        messageRepository.save(message);
    }

    @Override
    public MessageResponse editMessage(String username, Long messageId, String newContent) {
        Message message = getMessageById(messageId);
        ApplicationUser user = getUserByUsername(username);

        if (!message.getSender().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to edit this message.");
        }

        message.setContent(newContent);
        messageRepository.save(message);

        return messageResponseBuilder.build(message);
    }

    @Override
    public void deleteMessage(String username, Long messageId) {
        Message message = getMessageById(messageId);
        ApplicationUser user = getUserByUsername(username);

        if (message.getSender().getUsername().equals(username)) {
            message.setDeletedForSender(true);
        }
        else if (message.getRecipient().getUsername().equals(username)) {
            message.setDeletedForReceiver(true);
        }
        else {
            throw new AccessDeniedException("You are not authorized to delete this message.");
        }

        messageRepository.save(message);
    }

}
