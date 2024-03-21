package com.innowise.services;

import com.innowise.dto.requestDto.TicketChangeStatusRequestDto;
import com.innowise.dto.requestDto.TicketRequestDto;
import com.innowise.dto.responseDto.TicketResponseDto;
import com.innowise.domain.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    TicketResponseDto save(TicketRequestDto ticketRequestDto);

    List<TicketResponseDto> findAll();

    TicketResponseDto update(TicketRequestDto ticketRequestDto);

    void delete(Integer id);

    TicketResponseDto findById(Integer id);

    TicketResponseDto updateStatus(TicketChangeStatusRequestDto ticketChangeStatusRequestDto);

    Optional<Ticket> findByIdService(Integer id);

    boolean existsByIdService(Integer id);
}
