package com.innowise.dto.request;

import com.innowise.domain.enums.TicketState;

// For different status changes
public record ChangeTicketStatusRequest(
        Integer ticketId,
        TicketState state) {
}
