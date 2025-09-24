package com.innocodes.live_poll.controller;

import com.innocodes.live_poll.dto.PollRequestDTO;
import com.innocodes.live_poll.dto.PollResponseDTO;
import com.innocodes.live_poll.dto.VoteRequestDTO;
import com.innocodes.live_poll.event.VoteEvent;
import com.innocodes.live_poll.service.PollService;
import com.innocodes.live_poll.service.VoteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/polls")
public class PollController {

    private final PollService pollService;

    private final VoteService voteService;

    @PostMapping("/create-poll")
    public ResponseEntity<PollResponseDTO> createPoll(@Valid @RequestBody PollRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pollService.createPoll(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollResponseDTO> getPoll(@PathVariable UUID id) {
        return ResponseEntity.ok(pollService.getPoll(id));
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<Void> castVote(@PathVariable UUID id, @Valid @RequestBody VoteRequestDTO dto) {
        VoteEvent event = new VoteEvent();
        event.setUser(dto.getUsername());
        event.setPollId(id);
        event.setOption(dto.getOption());
        voteService.publishVote(event);
        return ResponseEntity.accepted().build();
    }
}