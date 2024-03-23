package com.innowise.dto.request;

import com.innowise.domain.enums.TicketState;
import com.innowise.util.validation.TicketStateValidation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// For different status changes
public record ChangeTicketStatusRequest(
        @NotNull
        @Min(1)
        Integer ticketId,

        @NotNull
        //TODO Проверить работоспособность этой аннотации :)
        @TicketStateValidation(anyOf = {TicketState.NEW, TicketState.APPROVED,
                TicketState.IN_PROGRESS, TicketState.DECLINED,
                TicketState.DONE, TicketState.CANCELED, TicketState.DRAFT})
        TicketState state) {
}
