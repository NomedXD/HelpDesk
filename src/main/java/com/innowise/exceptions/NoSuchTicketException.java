package com.innowise.exceptions;

public class NoSuchTicketException extends IllegalArgumentException {
    private final Integer ticketId;

    public NoSuchTicketException(Integer ticketId) {
        super(String.format("Ticket with id %d is not found in DB", ticketId));
        this.ticketId = ticketId;
    }
}
