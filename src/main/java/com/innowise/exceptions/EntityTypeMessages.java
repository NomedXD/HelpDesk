package com.innowise.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EntityTypeMessages {
    ATTACHMENT_MESSAGE("Attachment with id %d is not found in DB"),
    CATEGORY_MESSAGE("Category with id %d is not found in DB"),
    COMMENT_MESSAGE("Comment with id %d is not found in DB"),
    FEEDBACK_MESSAGE("Feedback with id %d is not found in DB"),
    HISTORY_MESSAGE("History with id %d is not found in DB"),
    TICKET_MESSAGE("Ticket with id %d is not found in DB"),
    USER_MESSAGE("User with id %d is not found in DB");

    private final String message;
}
