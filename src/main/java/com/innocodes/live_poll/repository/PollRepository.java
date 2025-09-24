package com.innocodes.live_poll.repository;

import com.innocodes.live_poll.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PollRepository extends JpaRepository<Poll, UUID> {
}
