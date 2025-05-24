package com.x.backend.services.message;

import com.x.backend.dto.message.request.SendMessageRequest;
import com.x.backend.dto.message.response.MessageResponse;

public interface MessageService {

    MessageResponse sendMessage(String senderUsername, SendMessageRequest req);

}
