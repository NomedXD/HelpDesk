package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketHasFeedbackException extends IllegalArgumentException {
    private final Integer ticketId;

    public TicketHasFeedbackException(Integer ticketId) {
        super(String.format("Ticket %d already has feedback by owner", ticketId));
        this.ticketId = ticketId;
    }
}
