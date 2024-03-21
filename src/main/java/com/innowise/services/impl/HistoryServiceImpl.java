package com.innowise.services.impl;

import com.innowise.dto.mappers.HistoryListMapper;
import com.innowise.dto.mappers.HistoryMapper;
import com.innowise.dto.responseDto.HistoryResponseDto;
import com.innowise.domain.History;
import com.innowise.exceptions.NoSuchHistoryException;
import com.innowise.exceptions.NoSuchTicketException;
import com.innowise.repositories.HistoryRepository;
import com.innowise.services.HistoryService;
import com.innowise.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;
    private final HistoryListMapper historyListMapper;

    private final TicketService ticketService;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository,
                              HistoryMapper historyMapper, HistoryListMapper historyListMapper, TicketService ticketService) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
        this.historyListMapper = historyListMapper;
        this.ticketService = ticketService;
    }

    @Override
    public History saveService(History history) {
        return historyRepository.save(history);
    }

    @Override
    public List<HistoryResponseDto> findAll() {
        return historyListMapper.toHistoryResponseDtoList(historyRepository.findAll());
    }

    @Override
    public void delete(Integer id) {
        if (historyRepository.existsById(id)) {
            historyRepository.delete(id);
        } else {
            throw new NoSuchHistoryException(id);
        }
    }

    @Override
    public HistoryResponseDto findById(Integer id) {
        return historyMapper.toHistoryResponseDto(historyRepository.findById(id).orElseThrow(() -> new NoSuchHistoryException(id)));
    }

    @Override
    public List<HistoryResponseDto> findAllByTicketId(Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            return historyListMapper.toHistoryResponseDtoList(historyRepository.findAllByTicketId(ticketId));
        } else {
            throw new NoSuchTicketException(ticketId);
        }
    }
}
