package com.innowise.services;

import com.innowise.dto.responseDto.HistoryResponseDto;
import com.innowise.domain.History;

import java.util.List;

public interface HistoryService {
    History saveService(History history);

    List<HistoryResponseDto> findAll();

    void delete(Integer id);

    HistoryResponseDto findById(Integer id);

    List<HistoryResponseDto> findAllByTicketId(Integer ticketId);
}
