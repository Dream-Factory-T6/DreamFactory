package com.DreamFactory.DF.chat;

import com.DreamFactory.DF.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class MessageTest {
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
    }

    @Test
    void testMessageCreation() {
        assertNotNull(message);
        assertEquals(1L, message.getId());
        assertEquals("Hello, world!", message.getContent());
        assertEquals(user, message.getUser());
        assertEquals("general", message.getRoomId());
        assertEquals(Message.MessageType.CHAT, message.getMessageType());
    }

    @Test
    void testMessageTypes() {
        message.setMessageType(Message.MessageType.JOIN);
        assertEquals(Message.MessageType.JOIN, message.getMessageType());

        message.setMessageType(Message.MessageType.LEAVE);
        assertEquals(Message.MessageType.LEAVE, message.getMessageType());
    }

    @Test
    void testMessageEquality() {
        Message message2 = new Message();
        message2.setId(1L);
        message2.setContent("Hello, world!");
        message2.setUser(user);
        message2.setRoomId("general");

        assertEquals(message.getId(), message2.getId());
        assertEquals(message.getContent(), message2.getContent());
        assertEquals(message.getUser(), message2.getUser());
        assertEquals(message.getRoomId(), message2.getRoomId());
    }
}