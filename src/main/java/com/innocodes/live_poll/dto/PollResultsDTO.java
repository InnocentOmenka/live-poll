package com.innocodes.live_poll.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class PollResultsDTO {
    private UUID id;
    private String question;
    private Map<String, Long> tally; // e.g., {"A": 5, "B": 3, "C": 0}
}