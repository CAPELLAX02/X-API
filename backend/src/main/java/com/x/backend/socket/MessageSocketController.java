package com.x.backend.socket;

import com.x.backend.dto.message.request.SendMessageRequest;
import com.x.backend.dto.message.response.MessageResponse;
import com.x.backend.services.message.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Handles incoming WebSocket messages related to direct messaging between users.
 *
 * <p>This controller receives messages from authenticated clients at
 * <code>/app/chat.send</code>, processes them via {@link MessageService},
 * and returns an acknowledgment back to the sender through
 * the <code>/user/queue/ack</code> destination.</p>
 *
 * <p>The current user is automatically resolved via the WebSocket security context.</p>
 */
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

    @MessageMapping("/chat.read")
    @SendToUser("/queue/read-status")
    public void handleReadMessage(
            @Payload Long messageId,
            Principal user
    ) {
        String username = user.getName();
        messageService.markMessageAsRead(username, messageId);
    }

}
