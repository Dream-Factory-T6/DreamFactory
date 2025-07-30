package com.DreamFactory.DF.chat;

import com.DreamFactory.DF.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {
    private final MessageService messageService;

    @GetMapping("/room/{roomId}/messages")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ChatMessage>> getRoomMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "50") int limit) {

        List<ChatMessage> messages = messageService.getRoomMessages(roomId, limit);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<String>> getAvailableRooms() {
        List<String> rooms = List.of("general", "support", "random");
        return ResponseEntity.ok(rooms);
    }
}