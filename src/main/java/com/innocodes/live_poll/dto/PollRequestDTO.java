package com.innocodes.live_poll.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class PollRequestDTO {
    @NotBlank(message = "Question is required")
    private String question;

    @NotEmpty(message = "At least one option is required")
    private List<String> options;

    @FutureOrPresent(message = "Scheduled start time must be in the present or future")
    private Instant scheduledStartTime;
}