package com.innocodes.live_poll.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VoteRequestDTO {
    @NotBlank(message = "User is required")
    private String username;

    @NotBlank(message = "Option is required")
    private String option;
}