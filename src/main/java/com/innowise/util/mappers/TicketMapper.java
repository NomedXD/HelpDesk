package com.innowise.util.mappers;

import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.domain.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HistoryListMapper.class, CommentListMapper.class, AttachmentListMapper.class})
public interface TicketMapper {
    @Mapping(source = "categoryId", target = "category.id")
    Ticket toTicket(CreateTicketRequest request);


    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "owner.email", target = "ownerEmail")
    @Mapping(source = "owner.role", target = "ownerRole")
    @Mapping(source = "approver.email", target = "approverEmail")
    @Mapping(source = "assignee.email", target = "assigneeEmail")
    @Mapping(source = "histories", target = "historyResponseList")
    @Mapping(source = "comments", target = "commentResponseList")
    TicketResponse toTicketResponseDto(Ticket ticket);
}
