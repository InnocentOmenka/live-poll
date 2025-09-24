package com.innocodes.live_poll.service;

import com.innocodes.live_poll.dto.PollRequestDTO;
import com.innocodes.live_poll.dto.PollResponseDTO;
import com.innocodes.live_poll.entity.Poll;
import com.innocodes.live_poll.exception.PollNotFoundException;
import com.innocodes.live_poll.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;

    private final ModelMapper modelMapper;

    public PollResponseDTO createPoll(PollRequestDTO dto) {
        Poll poll = modelMapper.map(dto, Poll.class);
        poll = pollRepository.save(poll);
        return modelMapper.map(poll, PollResponseDTO.class);
    }

    public PollResponseDTO getPoll(UUID id) {
        Poll poll = getPollEntity(id);
        return modelMapper.map(poll, PollResponseDTO.class);
    }

    public Poll getPollEntity(UUID id) {
        return pollRepository.findById(id)
                .orElseThrow(() -> new PollNotFoundException("Poll with ID " + id + " not found"));
    }

    public boolean hasStarted(Poll poll) {
        return Instant.now().isAfter(poll.getScheduledStartTime());
    }
}