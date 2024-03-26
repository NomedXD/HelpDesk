package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketNotDraftException extends IllegalArgumentException {
    private final Integer ticketId;

    public TicketNotDraftException(Integer ticketId) {
        super(String.format("Ticket with id %d is not DRAFT stated, it's not able no change ticket", ticketId));
        this.ticketId = ticketId;
    }
}
