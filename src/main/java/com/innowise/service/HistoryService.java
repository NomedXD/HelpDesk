package com.innowise.service;

import com.innowise.controller.dto.response.HistoryResponseDto;

import java.util.List;

public interface HistoryService {
    HistoryResponseDto findById(Integer id);
    List<HistoryResponseDto> findAll();
    List<HistoryResponseDto> findAllByTicketId(Integer ticketId);

    void delete(Integer id);
}
