package com.DreamFactory.DF.chat;

import com.DreamFactory.DF.auth.AuthServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    private final AuthServiceHelper authServiceHelper;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                try {
                    Authentication authentication = authServiceHelper.getAuthentication(token);
                    accessor.setUser(authentication);
                    log.info("WebSocket user authenticated: {}", authentication.getName());
                } catch (Exception e) {
                    log.error("WebSocket authentication failed: {}", e.getMessage());
                    return null;
                }
            } else {
                log.warn("No valid Authorization header found in WebSocket connection");
                return null;
            }
        }
        return message;
    }
}