package com.x.backend.utils.builder;

import com.x.backend.dto.message.response.MessageResponse;
import com.x.backend.models.message.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageResponseBuilder {

    public MessageResponse build(Message m) {
        return new MessageResponse(
                m.getId(),
                m.getSender().getId(),
                m.getSender().getUsername(),
                m.getRecipient().getId(),
                m.getContent(),
                m.isRead(),
                m.getSentAt()
        );
    }

}