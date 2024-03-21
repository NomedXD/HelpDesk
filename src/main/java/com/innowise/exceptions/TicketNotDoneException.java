package com.innowise.exceptions;

public class TicketNotDoneException extends IllegalArgumentException {
    private final Integer ticketId;

    public TicketNotDoneException(Integer ticketId) {
        super(String.format("Ticket with id %d is not DONE, it's not able no leave feedback", ticketId));
        this.ticketId = ticketId;
    }
}
