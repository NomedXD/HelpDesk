package com.innowise.exceptions;

import org.hibernate.boot.model.naming.IllegalIdentifierException;

public class TicketNotDraftException extends IllegalIdentifierException {
    private final Integer ticketId;

    public TicketNotDraftException(Integer ticketId) {
        super(String.format("Ticket with id %d is not DRAFT stated, it's not able no change ticket", ticketId));
        this.ticketId = ticketId;
    }
}
