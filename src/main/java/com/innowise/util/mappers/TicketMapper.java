package com.innowise.util.mappers;

import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.request.UpdateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.domain.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HistoryListMapper.class, CommentListMapper.class, AttachmentListMapper.class})
public interface TicketMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(source = "categoryId", target = "category.id")
    Ticket toTicket(CreateTicketRequest request);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "owner.firstName", target = "ownerName")
    @Mapping(source = "approver.firstName", target = "approverName")
    @Mapping(source = "assignee.firstName", target = "assigneeName")
    TicketResponse toTicketResponseDto(Ticket ticket);
}
