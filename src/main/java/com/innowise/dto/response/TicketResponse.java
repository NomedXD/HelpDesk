package com.innowise.dto.response;

import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.TicketUrgency;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TicketResponse{
    private Integer id;
    private String name;
    private LocalDate createdOn;
    private TicketState state;
    private TicketUrgency urgency;
    private String categoryName;
    private LocalDate desiredResolutionDate;
    private String ownerName;
    private String approverName;
    private String assigneeName;
    private List<AttachmentResponse> attachments;
    private String description;
    private List<HistoryResponse> historyResponseList;
    private List<CommentResponse> commentResponseList;
}
