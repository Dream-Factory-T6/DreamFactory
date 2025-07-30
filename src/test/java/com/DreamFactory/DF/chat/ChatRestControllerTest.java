package com.DreamFactory.DF.chat;

import com.DreamFactory.DF.chat.dto.ChatMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ChatRestControllerTest {
    @Mock
    private MessageService messageService;

    @InjectMocks
    private ChatRestController chatRestController;

    @Test
    void testGetRoomMessages() {
        String roomId = "general";
        int limit = 20;

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hello");
        chatMessage.setSender("testuser");
        chatMessage.setRoomId(roomId);
        chatMessage.setMessageType(Message.MessageType.CHAT);
        chatMessage.setTimestamp(LocalDateTime.now());

        List<ChatMessage> expectedMessages = Arrays.asList(chatMessage);

        when(messageService.getRoomMessages(roomId, limit)).thenReturn(expectedMessages);

        ResponseEntity<List<ChatMessage>> response = chatRestController.getRoomMessages(roomId, limit);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Hello", response.getBody().get(0).getContent());
        assertEquals("testuser", response.getBody().get(0).getSender());
    }

    @Test
    void testGetRoomMessages_WithDefaultLimit() {
        String roomId = "general";
        int defaultLimit = 50;

        List<ChatMessage> expectedMessages = Arrays.asList();
        when(messageService.getRoomMessages(roomId, defaultLimit)).thenReturn(expectedMessages);

        ResponseEntity<List<ChatMessage>> response = chatRestController.getRoomMessages(roomId, defaultLimit);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetAvailableRooms() {
        ResponseEntity<List<String>> response = chatRestController.getAvailableRooms();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertTrue(response.getBody().contains("general"));
        assertTrue(response.getBody().contains("support"));
        assertTrue(response.getBody().contains("random"));
    }

    @Test
    void testGetRoomMessages_EmptyList() {
        String roomId = "empty-room";
        int limit = 10;

        when(messageService.getRoomMessages(roomId, limit)).thenReturn(Arrays.asList());

        ResponseEntity<List<ChatMessage>> response = chatRestController.getRoomMessages(roomId, limit);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }
}