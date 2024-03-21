package com.innowise.dto.mappers;

import com.innowise.dto.requestDto.TicketRequestDto;
import com.innowise.dto.responseDto.TicketResponseDto;
import com.innowise.domain.Ticket;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = FeedbackMapper.class)
public interface TicketListMapper {
    List<Ticket> toTicketList(List<TicketRequestDto> ticketRequestDtoList);

    List<TicketResponseDto> toTicketResponseDtoList(List<Ticket> ticketList);
}
