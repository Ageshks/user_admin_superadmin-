package admin_user.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import admin_user.model.SupportChat.Message;
import admin_user.repositories.MessageRepository;

@Service
public class SupportChatService {

    @Autowired
    private MessageRepository messageRepository; // Assuming you have a MessageRepository

    public Message saveChatMessage(Message message) {
        // Set the timestamp for when the message is sent
        message.setSentAt(LocalDateTime.now());

        // Save the message using the repository
        return messageRepository.save(message);
    }
}
