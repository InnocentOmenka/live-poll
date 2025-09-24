package com.innocodes.live_poll.exception;

public class PollNotStartedException extends RuntimeException {
    public PollNotStartedException(String message) {
        super(message);
    }
}