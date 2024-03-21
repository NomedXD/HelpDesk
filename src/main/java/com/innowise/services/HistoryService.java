package com.innowise.services;

import com.innowise.dto.response.HistoryResponse;
import com.innowise.domain.History;

import java.util.List;

public interface HistoryService {
    History saveService(History history);

    List<HistoryResponse> findAll();

    void delete(Integer id);

    HistoryResponse findById(Integer id);

    List<HistoryResponse> findAllByTicketId(Integer ticketId);
}
