package com.innowise.controller.dto.mapper;

import com.innowise.controller.dto.requestDto.TicketRequestDto;
import com.innowise.controller.dto.responseDto.TicketResponseDto;
import com.innowise.domain.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HistoryListMapper.class, CommentListMapper.class, AttachmentListMapper.class})
public interface TicketMapper {
    @Mapping(source = "creatorId", target = "owner.id")
    @Mapping(source = "categoryId", target = "category.id")
    Ticket toTicket(TicketRequestDto tweetRequestTo);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "owner.firstName", target = "ownerName")
    @Mapping(source = "approver.firstName", target = "approverName")
    @Mapping(source = "assignee.firstName", target = "assigneeName")
    TicketResponseDto toTicketResponseDto(Ticket ticket);
}
