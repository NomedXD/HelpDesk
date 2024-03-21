package com.innowise.service;

import com.innowise.controller.dto.requestDto.TicketChangeStatusRequestDto;
import com.innowise.controller.dto.requestDto.TicketRequestDto;
import com.innowise.controller.dto.responseDto.TicketResponseDto;

import java.util.List;

public interface TicketService {
    TicketResponseDto findById(Integer id);
    List<TicketResponseDto> findAll();

    TicketResponseDto save(TicketRequestDto ticket);
    TicketResponseDto update(TicketRequestDto ticket);
    TicketResponseDto updateStatus(TicketChangeStatusRequestDto changeStatusDto);

    void delete(Integer id);
}
