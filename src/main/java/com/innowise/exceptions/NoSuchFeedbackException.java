package com.innowise.exceptions;

public class NoSuchFeedbackException extends IllegalArgumentException {
    private final Integer feedbackId;

    public NoSuchFeedbackException(Integer feedbackId) {
        super(String.format("Feedback with id %d is not found in DB", feedbackId));
        this.feedbackId = feedbackId;
    }
}
