package com.innowise.util.mappers;

import com.innowise.dto.request.UpdateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.domain.Ticket;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TicketMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TicketListMapper {
    List<Ticket> toTicketList(List<UpdateTicketRequest> updateTicketRequestList);

    List<TicketResponse> toTicketResponseDtoList(List<Ticket> ticketList);
}
