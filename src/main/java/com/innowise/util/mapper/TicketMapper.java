package com.innowise.util.mapper;

import com.innowise.controller.dto.request.TicketRequest;
import com.innowise.controller.dto.response.TicketResponseDto;
import com.innowise.domain.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HistoryListMapper.class, CommentListMapper.class, AttachmentListMapper.class})
public interface TicketMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "categoryId", target = "category.id")
    Ticket toTicket(TicketRequest tweetRequestTo);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "owner.firstName", target = "ownerName")
    @Mapping(source = "approver.firstName", target = "approverName")
    @Mapping(source = "assignee.firstName", target = "assigneeName")
    TicketResponseDto toTicketResponseDto(Ticket ticket);
}
