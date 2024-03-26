package com.innowise.services.impl;

import com.innowise.exceptions.EntityTypeMessages;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.util.mappers.HistoryListMapper;
import com.innowise.util.mappers.HistoryMapper;
import com.innowise.dto.response.HistoryResponse;
import com.innowise.domain.History;
import com.innowise.repositories.HistoryRepository;
import com.innowise.services.HistoryService;
import com.innowise.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;
    private final HistoryListMapper historyListMapper;

    private TicketService ticketService;

    @Override
    public History saveService(History history) {
        return historyRepository.save(history);
    }

    @Override
    public List<HistoryResponse> findAll() {
        return historyListMapper.toHistoryResponseDtoList(historyRepository.findAll());
    }

    @Override
    public void delete(Integer id) {
        if (historyRepository.existsById(id)) {
            historyRepository.delete(id);
        } else {
            throw new NoSuchEntityIdException(EntityTypeMessages.HISTORY_MESSAGE, id);
        }
    }

    @Override
    public HistoryResponse findById(Integer id) {
        return historyMapper.toHistoryResponseDto(historyRepository.findById(id).orElseThrow(() ->
                new NoSuchEntityIdException(EntityTypeMessages.HISTORY_MESSAGE, id)));
    }

    @Override
    public List<HistoryResponse> findAllByTicketId(Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            return historyListMapper.toHistoryResponseDtoList(historyRepository.findAllByTicketId(ticketId));
        } else {
            throw new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, ticketId);
        }
    }

    @Autowired
    @Lazy
    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }
}
