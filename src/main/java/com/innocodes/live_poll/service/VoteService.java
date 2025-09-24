package com.innocodes.live_poll.service;


import com.innocodes.live_poll.config.RabbitMQConfig;
import com.innocodes.live_poll.entity.Poll;
import com.innocodes.live_poll.entity.Vote;
import com.innocodes.live_poll.event.VoteEvent;
import com.innocodes.live_poll.exception.InvalidOptionException;
import com.innocodes.live_poll.exception.PollNotFoundException;
import com.innocodes.live_poll.exception.PollNotStartedException;
import com.innocodes.live_poll.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteService {


    private final PollService pollService;

    private final VoteRepository voteRepository;

    private final RabbitTemplate rabbitTemplate;

    private final WebSocketService webSocketService;

    public void publishVote(VoteEvent event) {
        Poll poll = pollService.getPollEntity(event.getPollId());
        if (!pollService.hasStarted(poll)) {
            throw new PollNotStartedException("Poll with ID " + event.getPollId() + " has not started");
        }
        if (!poll.getOptions().contains(event.getOption())) {
            throw new InvalidOptionException("Invalid option '" + event.getOption() + "' for poll ID " + event.getPollId());
        }
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event);
        log.info("Published vote event for pollId {} by user {}", event.getPollId(), event.getUser());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processVote(VoteEvent event) {
        try {
            Poll poll = pollService.getPollEntity(event.getPollId()); // Use getPollEntity
            Vote vote = new Vote();
            vote.setPoll(poll);
            vote.setUsername(event.getUser());
            vote.setOption(event.getOption());
            vote.setTimestamp(Instant.now());
            voteRepository.save(vote);
            log.info("Processed vote for pollId {} by user {}", event.getPollId(), event.getUser());

            Map<String, Long> tally = calculateTally(poll.getId());
            webSocketService.broadcastResults(poll.getId(), tally);
        } catch (PollNotFoundException e) {
            log.error("Failed to process vote for pollId {}: {}", event.getPollId(), e.getMessage());
        }
    }

    private Map<String, Long> calculateTally(UUID pollId) {
        List<Vote> votes = voteRepository.findByPollId(pollId);
        return votes.stream()
                .collect(Collectors.groupingBy(Vote::getOption, Collectors.counting()));
    }
}