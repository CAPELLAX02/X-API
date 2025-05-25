package com.x.backend.socket;

import com.x.backend.dto.message.request.SendMessageRequest;
import com.x.backend.dto.message.response.MessageResponse;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.services.message.MessageService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageSocketController {

    private final MessageService messageService;

    public MessageSocketController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat.send")
    @SendToUser("/queue/ack")
    public MessageResponse handleSendMessage(
            @Payload SendMessageRequest req,
            Principal user
    ) {
        String username = user.getName();
        return messageService.sendMessage(username, req);
    }

}
