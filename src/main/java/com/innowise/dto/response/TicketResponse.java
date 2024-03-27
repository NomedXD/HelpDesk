package com.innowise.dto.response;

import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.TicketUrgency;

import java.time.LocalDate;
import java.util.List;

public record TicketResponse(
        Integer id,
        LocalDate createdOn,
        TicketState state,
        TicketUrgency urgency,
        String categoryName,
        LocalDate desiredResolutionDate,
        String ownerName,
        String approverName,
        String assigneeName,
        // TODO Multiple files problem (ycovich)
        //List<AttachmentResponse> attachments,
        String description,
        List<HistoryResponse> historyResponseList,
        List<CommentResponse> commentResponseList) {
}
