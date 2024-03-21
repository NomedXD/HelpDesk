package com.innowise.exceptions;

public class NotOwnerTicketException extends IllegalArgumentException {
    private final Integer ownerId;
    private final Integer ticketId;

    public NotOwnerTicketException(Integer ownerId, Integer ticketId) {
        super(String.format("User with id %d is not owner of ticket %d", ownerId, ticketId));
        this.ownerId = ownerId;
        this.ticketId = ticketId;
    }
}
