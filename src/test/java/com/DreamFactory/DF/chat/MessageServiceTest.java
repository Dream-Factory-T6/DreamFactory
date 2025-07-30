package com.DreamFactory.DF.chat;

import com.DreamFactory.DF.chat.dto.ChatMessage;
import com.DreamFactory.DF.user.model.User;
import com.DreamFactory.DF.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private MessageService messageService;

    private User user;
    private Message message;
    private ChatMessage chatMessage;

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
    void testSendMessage_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        assertDoesNotThrow(() -> messageService.sendMessage(chatMessage, "testuser"));

        verify(userRepository).findByUsername("testuser");
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void testSendMessage_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> messageService.sendMessage(chatMessage, "nonexistent"));

        verify(userRepository).findByUsername("nonexistent");
        verify(messageRepository, never()).save(any());
    }

    @Test
    void testGetRoomMessages() {
        List<Message> messages = Arrays.asList(message);
        when(messageRepository.findRecentMessagesByRoomId("general", 10)).thenReturn(messages);

        List<ChatMessage> result = messageService.getRoomMessages("general", 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hello, world!", result.get(0).getContent());
        assertEquals("testuser", result.get(0).getSender());
        verify(messageRepository).findRecentMessagesByRoomId("general", 10);
    }

    @Test
    void testSendJoinNotification() {
        messageService.sendJoinNotification("general", "testuser");

        assertDoesNotThrow(() -> messageService.sendJoinNotification("general", "testuser"));
    }

    @Test
    void testSendLeaveNotification() {
        messageService.sendLeaveNotification("general", "testuser");

        assertDoesNotThrow(() -> messageService.sendLeaveNotification("general", "testuser"));
    }
}