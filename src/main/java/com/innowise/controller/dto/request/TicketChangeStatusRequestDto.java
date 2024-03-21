package com.innowise.controller.dto.request;

import com.innowise.domain.enums.TicketState;

// For different status changes
public record TicketChangeStatusRequestDto(
        Integer ticketId,
        TicketState state) {
}
