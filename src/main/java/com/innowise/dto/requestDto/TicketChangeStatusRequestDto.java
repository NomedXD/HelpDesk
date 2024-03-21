package com.innowise.dto.requestDto;

import com.innowise.domain.enums.TicketState;

// For different status changes
public record TicketChangeStatusRequestDto(
        Integer ticketId,
        TicketState state) {
}
