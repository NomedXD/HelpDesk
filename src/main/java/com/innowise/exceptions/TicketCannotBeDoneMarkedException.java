package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketCannotBeDoneMarkedException extends IllegalArgumentException{
    private final Integer ticketId;

    public TicketCannotBeDoneMarkedException(Integer ticketId) {
        super(String.format("Ticket with id %d cannot be DONE marked due to insufficient number of attributes", ticketId));
        this.ticketId = ticketId;
    }
}
