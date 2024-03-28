package com.innowise.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EntityTypeMessages {
    ATTACHMENT_MESSAGE("Attachment with id %d is not found"),
    CATEGORY_MESSAGE("Category with id %d is not found"),
    COMMENT_MESSAGE("Comment with id %d is not found"),
    FEEDBACK_MESSAGE("Feedback with id %d is not found"),
    HISTORY_MESSAGE("History with id %d is not found"),
    TICKET_MESSAGE("Ticket with id %d is not found"),
    USER_MESSAGE("User with id %d is not found");

    private final String message;
}
