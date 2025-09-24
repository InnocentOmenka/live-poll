package com.innocodes.live_poll.repository;

import com.innocodes.live_poll.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    List<Vote> findByPollId(UUID pollId);
}
