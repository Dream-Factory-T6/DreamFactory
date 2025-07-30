package com.DreamFactory.DF.chat;

import com.DreamFactory.DF.chat.dto.ChatMessage;
import com.DreamFactory.DF.user.model.User;
import com.DreamFactory.DF.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(ChatMessage chatMessage, String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            Message message = new Message();
            message.setContent(chatMessage.getContent());
            message.setUser(user);
            message.setRoomId(chatMessage.getRoomId());
            message.setMessageType(chatMessage.getMessageType());

            Message savedMessage = messageRepository.save(message);

            ChatMessage responseMessage = ChatMessage.fromMessage(savedMessage);

            messagingTemplate.convertAndSend("/topic/room/" + chatMessage.getRoomId(), responseMessage);

            log.info("Message sent to room {} by user {}: {}",
                    chatMessage.getRoomId(), username, chatMessage.getContent());

        } catch (Exception e) {
            log.error("Error sending message: ", e);
            throw new RuntimeException("Failed to send message", e);
        }
    }

    public List<ChatMessage> getRoomMessages(String roomId, int limit) {
        List<Message> messages = messageRepository.findRecentMessagesByRoomId(roomId, limit);
        return messages.stream()
                .map(ChatMessage::fromMessage)
                .collect(Collectors.toList());
    }

    public void sendJoinNotification(String roomId, String username) {
        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setContent(username + " joined the room");
        joinMessage.setSender("System");
        joinMessage.setRoomId(roomId);
        joinMessage.setMessageType(Message.MessageType.JOIN);
        joinMessage.setTimestamp(java.time.LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/room/" + roomId, joinMessage);
        log.info("Join notification sent for user {} in room {}", username, roomId);
    }

    public void sendLeaveNotification(String roomId, String username) {
        ChatMessage leaveMessage = new ChatMessage();
        leaveMessage.setContent(username + " left the room");
        leaveMessage.setSender("System");
        leaveMessage.setRoomId(roomId);
        leaveMessage.setMessageType(Message.MessageType.LEAVE);
        leaveMessage.setTimestamp(java.time.LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/room/" + roomId, leaveMessage);
        log.info("Leave notification sent for user {} in room {}", username, roomId);
    }
}