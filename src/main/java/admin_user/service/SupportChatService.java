package admin_user.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import admin_user.model.SupportChat;
import admin_user.repositories.SupportChatRepository;

@Service
public class SupportChatService {
    @Autowired
    private SupportChatRepository supportChatRepository;

    public SupportChat createSupportTicket(Long userId, String subject, String initialMessage) {
        SupportChat supportChat = new SupportChat();
        supportChat.setUserId(userId);
        supportChat.setSubject(subject);
        supportChat.setStatus(SupportChat.TicketStatus.OPEN);
        supportChat.setCreatedAt(LocalDateTime.now());

        // Create initial message
        SupportChat.Message message = new SupportChat.Message();
        message.setSenderId(userId);
        message.setMessageContent(initialMessage);
        message.setSentAt(LocalDateTime.now());
        message.setMessageType(SupportChat.Message.MessageType.USER);

        supportChat.setMessages(List.of(message));

        return supportChatRepository.save(supportChat);
    }

// In SupportChatService
        public SupportChat.Message sendMessage(Long ticketId, Long senderId, String messageContent, SupportChat.Message.MessageType messageType)    {
        SupportChat supportChat = supportChatRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Support ticket not found"));

        SupportChat.Message message = new SupportChat.Message();
        message.setSenderId(senderId);
        message.setMessageContent(messageContent);
        message.setSentAt(LocalDateTime.now());
        message.setMessageType(messageType);

        supportChat.getMessages().add(message);
        supportChatRepository.save(supportChat);

        return message;
    }

    public List<SupportChat> getUserSupportTickets(Long userId) {
        return supportChatRepository.findByUserId(userId);
    }

    public SupportChat getSupportTicketDetails(Long ticketId) {
        return supportChatRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Support ticket not found"));
    }

    public SupportChat closeSupportTicket(Long ticketId) {
        SupportChat supportChat = supportChatRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Support ticket not found"));

        supportChat.setStatus(SupportChat.TicketStatus.CLOSED);
        return supportChatRepository.save(supportChat);
    }
    
}
