package com.innocodes.live_poll.config;

import com.innocodes.live_poll.websocket.PollWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    private final PollWebSocketHandler pollWebSocketHandler;

    public WebSocketConfig(PollWebSocketHandler pollWebSocketHandler) {
        this.pollWebSocketHandler = pollWebSocketHandler;
        logger.info("WebSocketConfig initialized with PollWebSocketHandler");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        logger.info("Registering WebSocket handler for /ws");
        registry.addHandler(pollWebSocketHandler, "/ws").setAllowedOrigins("*");
    }
}