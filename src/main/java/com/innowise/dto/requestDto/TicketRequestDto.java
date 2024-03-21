package com.innowise.dto.requestDto;

import com.innowise.domain.enums.TicketUrgency;

import java.time.LocalDate;

// Only for Creation and editing by creator
public record TicketRequestDto(
        Integer id,
        Integer categoryId,
        Integer creatorId,
        String name,
        String description,
        TicketUrgency urgency,
        LocalDate desiredResolutionDate,
        // todo Think about this and multiple attachments
        byte[] attachment,
        String comment) {
}
