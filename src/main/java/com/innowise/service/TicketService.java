package com.innowise.service;

import com.innowise.controller.dto.request.TicketChangeStatusRequestDto;
import com.innowise.controller.dto.request.TicketRequest;
import com.innowise.controller.dto.response.TicketResponseDto;

import java.util.List;

public interface TicketService {
    TicketResponseDto findById(Integer id);
    List<TicketResponseDto> findAll();

    TicketResponseDto save(TicketRequest ticket);
//    TicketResponseDto update(TicketRequest ticket);
//    TicketResponseDto updateStatus(TicketChangeStatusRequestDto changeStatusDto);

    void delete(Integer id);
}
