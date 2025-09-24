package com.innocodes.live_poll.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class PollResponseDTO {
    private UUID id;
    private String question;
    private List<String> options;
    private Instant scheduledStartTime;
}