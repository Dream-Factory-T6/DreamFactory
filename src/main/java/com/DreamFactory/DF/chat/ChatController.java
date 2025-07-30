package com.DreamFactory.DF.chat;

import com.DreamFactory.DF.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage, Principal principal) {
        if (principal != null) {
            messageService.sendMessage(chatMessage, principal.getName());
        } else {
            log.warn("Attempted to send message without authentication");
        }
    }

    @MessageMapping("/chat.addUser")
    @SendToUser("/queue/reply")
    public void addUser(@Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor,
            Principal principal) {
        if (principal != null) {
            headerAccessor.getSessionAttributes().put("username", principal.getName());
            headerAccessor.getSessionAttributes().put("roomId", chatMessage.getRoomId());

            messageService.sendJoinNotification(chatMessage.getRoomId(), principal.getName());

            log.info("User {} joined room {}", principal.getName(), chatMessage.getRoomId());
        }
    }

    @MessageMapping("/chat.leaveRoom")
    public void leaveRoom(SimpMessageHeaderAccessor headerAccessor, Principal principal) {
        if (principal != null) {
            String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
            if (roomId != null) {
                messageService.sendLeaveNotification(roomId, principal.getName());
                log.info("User {} left room {}", principal.getName(), roomId);
            }
        }
    }
}