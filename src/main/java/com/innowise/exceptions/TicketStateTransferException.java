package com.innowise.exceptions;

import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketStateTransferException extends IllegalArgumentException {
    private final Integer ticketId;
    private final UserRole userRole;
    private final TicketState ticketState;

    public TicketStateTransferException(Integer ticketId, UserRole userRole, TicketState ticketState) {
        super(String.format("Ticket with id %d cannot change state to %s by user with role: %s", ticketId, ticketState, userRole.getAuthority()));
        this.ticketId = ticketId;
        this.userRole = userRole;
        this.ticketState = ticketState;
    }
}
