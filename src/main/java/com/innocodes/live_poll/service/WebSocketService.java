package com.innocodes.live_poll.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innocodes.live_poll.dto.PollResultsDTO;
import com.innocodes.live_poll.entity.Poll;
import com.innocodes.live_poll.websocket.PollWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);


    private final PollService pollService;

    private final ObjectMapper objectMapper;

    private final PollWebSocketHandler handler;

    public void broadcastResults(UUID pollId, Map<String, Long> tally) {
        try {
            Poll poll = pollService.getPollEntity(pollId);
            PollResultsDTO dto = new PollResultsDTO();
            dto.setId(pollId);
            dto.setQuestion(poll.getQuestion());
            dto.setTally(tally);
            String json = objectMapper.writeValueAsString(dto);
            handler.sendMessageToAll(json);
            logger.debug("Broadcasted poll results for pollId {}: {}", pollId, json);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize PollResultsDTO for pollId {}: {}", pollId, e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to broadcast results for pollId {}: {}", pollId, e.getMessage());
        }
    }
}