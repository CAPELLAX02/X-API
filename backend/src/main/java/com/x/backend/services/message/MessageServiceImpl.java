package com.x.backend.services.message;

import com.x.backend.dto.message.request.SendMessageRequest;
import com.x.backend.dto.message.response.MessageResponse;
import com.x.backend.exceptions.message.MessageNotFoundException;
import com.x.backend.exceptions.user.UserNotFoundByIdException;
import com.x.backend.models.message.Conversation;
import com.x.backend.models.message.Message;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.repositories.ConversationRepository;
import com.x.backend.repositories.MessageRepository;
import com.x.backend.utils.builder.MessageResponseBuilder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private final ApplicationUserRepository applicationUserRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageResponseBuilder messageResponseBuilder;

    public MessageServiceImpl(ApplicationUserRepository applicationUserRepository,
                              ConversationRepository conversationRepository,
                              MessageRepository messageRepository,
                              SimpMessagingTemplate messagingTemplate,
                              MessageResponseBuilder messageResponseBuilder
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.messageResponseBuilder = messageResponseBuilder;
    }

    @Override
    public MessageResponse sendMessage(String senderUsername, SendMessageRequest req) {
        // Determine the participants
        ApplicationUser sender = applicationUserRepository.findByUsername(senderUsername).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + senderUsername));
        ApplicationUser recipient = applicationUserRepository.findById(req.recipientId()).orElseThrow(() -> new UserNotFoundByIdException(req.recipientId()));

        // Find or create conversation between participants
        Conversation conversation = conversationRepository.findDirectConversationBetween(sender.getId(), recipient.getId())
                .orElseGet(() -> {
                    Conversation conv = new Conversation();
                    conv.setParticipants(Set.of(sender, recipient));
                    conv.setCreatedAt(LocalDateTime.now());
                    return conversationRepository.save(conv);
                });

        // Optional replyTo
        Message replyTo = null;
        if (req.replyToMessageId() != null) {
            replyTo = messageRepository.findById(req.replyToMessageId()).orElseThrow(() -> new MessageNotFoundException(req.replyToMessageId()));
        }

        // Create message
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(req.content());
        message.setReplyTo(replyTo);
        message.setSentAt(LocalDateTime.now());

        conversationRepository.save(conversation);

        // Prepare and send response
        MessageResponse messageResponse = messageResponseBuilder.build(message);

        messagingTemplate.convertAndSendToUser(
                recipient.getUsername(),
                "/queue/messages",
                messageResponse
        );

        return messageResponse;
    }

}
