package com.DreamFactory.DF.chat.dto;

import com.DreamFactory.DF.chat.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String content;
    private String sender;
    private String roomId;
    private Message.MessageType messageType;
    private LocalDateTime timestamp;

    public static ChatMessage fromMessage(Message message) {
        return new ChatMessage(
                message.getContent(),
                message.getUser().getUsername(),
                message.getRoomId(),
                message.getMessageType(),
                message.getCreatedAt());
    }
}