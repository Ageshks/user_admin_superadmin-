package admin_user.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import admin_user.model.SupportChat;
import admin_user.service.SupportChatService;

@RestController
@RequestMapping("/support-chat")
public class SupportChatController {
    @Autowired
    private SupportChatService supportChatService;

    @PostMapping("/create")
    public ResponseEntity<SupportChat> createSupportTicket(
        @RequestParam Long userId, 
        @RequestParam String subject, 
        @RequestParam String initialMessage
    ) {
        return ResponseEntity.ok(supportChatService.createSupportTicket(userId, subject, initialMessage));
    }

    // In SupportChatController
@PostMapping("/message")
public ResponseEntity<SupportChat.Message> sendSupportMessage(
    @RequestParam Long ticketId, 
    @RequestParam Long senderId, 
    @RequestParam String messageContent,
    @RequestParam SupportChat.Message.MessageType messageType
) {
        return ResponseEntity.ok(supportChatService.sendMessage(ticketId, senderId, messageContent, messageType));
    }

    @GetMapping("/tickets/{userId}")
    public ResponseEntity<List<SupportChat>> getUserSupportTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(supportChatService.getUserSupportTickets(userId));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<SupportChat> getSupportTicketDetails(@PathVariable Long ticketId) {
        return ResponseEntity.ok(supportChatService.getSupportTicketDetails(ticketId));
    }

    @PostMapping("/ticket/{ticketId}/close")
    public ResponseEntity<SupportChat> closeSupportTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(supportChatService.closeSupportTicket(ticketId));
    }
}