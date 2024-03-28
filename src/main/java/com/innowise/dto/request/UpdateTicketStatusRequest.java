package com.innowise.dto.request;

import com.innowise.domain.enums.TicketState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// For different status changes
public record UpdateTicketStatusRequest(
        @NotNull
        @Min(1)
        Integer ticketId,

        @NotNull
        TicketState state) {
}
