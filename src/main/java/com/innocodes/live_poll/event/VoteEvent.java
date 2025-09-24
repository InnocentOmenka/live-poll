package com.innocodes.live_poll.event;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class VoteEvent implements Serializable {
    private String user;
    private UUID pollId;
    private String option;
}