package admin_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import admin_user.model.SupportChat.Message;
import admin_user.service.SupportChatService;

@Controller
public class WebSocketChatController {

    @Autowired
    private SupportChatService supportChatService;

    @MessageMapping("/chat") // The message mapping endpoint
    @SendTo("/topic/messages") // This is the topic to which the message will be broadcasted
    public Message sendChatMessage(Message message) {
        // Handle the message, save it in the database, and return it for broadcasting
        return supportChatService.saveChatMessage(message);
    }
}
