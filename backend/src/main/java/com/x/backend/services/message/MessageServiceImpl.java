package com.x.backend.services.message;

import com.x.backend.dto.message.request.SendMessageRequest;
import com.x.backend.dto.message.response.MessageResponse;
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

import java.time.Instant;
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

    public MessageServiceImpl(ApplicationUserRepository applicationUserRepository,
                              ConversationRepository conversationRepository,
                              MessageRepository messageRepository,
                              MessageReadRepository messageReadRepository,
                              SimpMessagingTemplate messagingTemplate,
                              MessageResponseBuilder messageResponseBuilder
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.messageReadRepository = messageReadRepository;
        this.messagingTemplate = messagingTemplate;
        this.messageResponseBuilder = messageResponseBuilder;
    }

    @Override
    public MessageResponse sendMessage(String senderUsername, SendMessageRequest req) {
        ApplicationUser sender = applicationUserRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + senderUsername));

        ApplicationUser recipient = applicationUserRepository.findById(req.recipientId())
                .orElseThrow(() -> new UserNotFoundByIdException(req.recipientId()));

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
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException(messageId));
        ApplicationUser user = applicationUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!message.getRecipient().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only the recipient can mark this message as read.");
        }

        if (!message.isRead())  {
            message.setRead(true);
            MessageRead readMessage = new MessageRead();
            readMessage.setUser(user);
            readMessage.setMessage(message);
            readMessage.setReadAt(LocalDateTime.now());
            messageReadRepository.save(readMessage);
        }
    }

    @Override
    public MessageResponse editMessage(String username, Long messageId, String newContent) {
        return null;
    }

    @Override
    public void deleteMessage(String username, Long messageId) {

    }


}
