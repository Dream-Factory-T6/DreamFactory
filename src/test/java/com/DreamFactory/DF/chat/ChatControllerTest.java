package com.DreamFactory.DF.chat;

import com.DreamFactory.DF.chat.dto.ChatMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ActiveProfiles;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ChatControllerTest {
    @Mock
    private MessageService messageService;

    @Mock
    private Principal principal;

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ChatController chatController;

    @Test
    void testSendMessage_WithPrincipal() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hello");
        chatMessage.setRoomId("general");
        chatMessage.setMessageType(Message.MessageType.CHAT);

        when(principal.getName()).thenReturn("testuser");

        chatController.sendMessage(chatMessage, principal);

        verify(messageService).sendMessage(chatMessage, "testuser");
    }

    @Test
    void testSendMessage_WithoutPrincipal() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hello");
        chatMessage.setRoomId("general");
        chatMessage.setMessageType(Message.MessageType.CHAT);

        chatController.sendMessage(chatMessage, null);

        verify(messageService, never()).sendMessage(any(), any());
    }

    @Test
    void testAddUser_WithPrincipal() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Joined room");
        chatMessage.setRoomId("general");
        chatMessage.setMessageType(Message.MessageType.JOIN);

        when(principal.getName()).thenReturn("testuser");
        when(headerAccessor.getSessionAttributes()).thenReturn(new java.util.HashMap<>());

        chatController.addUser(chatMessage, headerAccessor, principal);

        verify(messageService).sendJoinNotification("general", "testuser");
    }

    @Test
    void testAddUser_WithoutPrincipal() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Joined room");
        chatMessage.setRoomId("general");
        chatMessage.setMessageType(Message.MessageType.JOIN);

        chatController.addUser(chatMessage, headerAccessor, null);

        verify(messageService, never()).sendJoinNotification(any(), any());
    }

    @Test
    void testLeaveRoom_WithPrincipal() {
        java.util.Map<String, Object> sessionAttributes = new java.util.HashMap<>();
        sessionAttributes.put("roomId", "general");

        when(principal.getName()).thenReturn("testuser");
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);

        chatController.leaveRoom(headerAccessor, principal);

        verify(messageService).sendLeaveNotification("general", "testuser");
    }

    @Test
    void testLeaveRoom_WithoutPrincipal() {
        chatController.leaveRoom(headerAccessor, null);

        verify(messageService, never()).sendLeaveNotification(any(), any());
    }
}