package com.innocodes.live_poll.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "poll")
@Data
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String question;
    @ElementCollection
    private List<String> options;
    private Instant scheduledStartTime;
}

