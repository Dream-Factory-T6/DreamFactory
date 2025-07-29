package com.DreamFactory.DF.chat.dto;

import com.DreamFactory.DF.chat.Message;
import com.DreamFactory.DF.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class ChatMessageTest {

    private ChatMessage chatMessage;
    private Message message;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        message = new Message();
        message.setId(1L);
        message.setContent("Hello, world!");
        message.setUser(user);
        message.setRoomId("general");
        message.setCreatedAt(LocalDateTime.now());
        message.setMessageType(Message.MessageType.CHAT);

        chatMessage = new ChatMessage();
        chatMessage.setContent("Hello, world!");
        chatMessage.setSender("testuser");
        chatMessage.setRoomId("general");
        chatMessage.setMessageType(Message.MessageType.CHAT);
        chatMessage.setTimestamp(LocalDateTime.now());
    }

    @Test
    void testChatMessageCreation() {
        assertNotNull(chatMessage);
        assertEquals("Hello, world!", chatMessage.getContent());
        assertEquals("testuser", chatMessage.getSender());
        assertEquals("general", chatMessage.getRoomId());
        assertEquals(Message.MessageType.CHAT, chatMessage.getMessageType());
        assertNotNull(chatMessage.getTimestamp());
    }

    @Test
    void testFromMessage() {
        ChatMessage convertedMessage = ChatMessage.fromMessage(message);

        assertNotNull(convertedMessage);
        assertEquals(message.getContent(), convertedMessage.getContent());
        assertEquals(message.getUser().getUsername(), convertedMessage.getSender());
        assertEquals(message.getRoomId(), convertedMessage.getRoomId());
        assertEquals(message.getMessageType(), convertedMessage.getMessageType());
        assertEquals(message.getCreatedAt(), convertedMessage.getTimestamp());
    }

    @Test
    void testChatMessageEquality() {
        ChatMessage chatMessage2 = new ChatMessage();
        chatMessage2.setContent("Hello, world!");
        chatMessage2.setSender("testuser");
        chatMessage2.setRoomId("general");
        chatMessage2.setMessageType(Message.MessageType.CHAT);

        assertEquals(chatMessage.getContent(), chatMessage2.getContent());
        assertEquals(chatMessage.getSender(), chatMessage2.getSender());
        assertEquals(chatMessage.getRoomId(), chatMessage2.getRoomId());
        assertEquals(chatMessage.getMessageType(), chatMessage2.getMessageType());
    }
}