package com.innowise.controller.dto.responseDto;

import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.TicketUrgency;

import java.time.LocalDate;
import java.util.List;

public record TicketResponseDto(
        LocalDate createdOn,
        TicketState state,
        TicketUrgency urgency,
        String categoryName,
        LocalDate desiredResolutionDate,
        String ownerName,
        String approverName,
        String assigneeName,
        // todo Think about this and multiple attachments
        List<AttachmentResponseDto> attachments,
        String description,
        List<HistoryResponseDto> historyResponseDtoList,
        List<CommentResponseDto> commentResponseDtoList) {
}
