package com.innowise.service.impl;

import com.innowise.controller.dto.mapper.HistoryListMapper;
import com.innowise.controller.dto.mapper.HistoryMapper;
import com.innowise.controller.dto.responseDto.HistoryResponseDto;
import com.innowise.repo.HistoryRepository;
import com.innowise.repo.TicketRepository;
import com.innowise.service.HistoryService;
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

    private final TicketRepository ticketRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository,
                              HistoryMapper historyMapper, HistoryListMapper historyListMapper, TicketRepository ticketRepository) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
        this.historyListMapper = historyListMapper;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public HistoryResponseDto findById(Integer id) {
        return historyMapper.toHistoryResponseDto(historyRepository.findById(id).orElseThrow(
                //todo throw Exception Not Found
        ));
    }

    @Override
    public List<HistoryResponseDto> findAll() {
        return historyListMapper.toHistoryResponseDtoList(historyRepository.findAll());
    }

    @Override
    public List<HistoryResponseDto> findAllByTicketId(Integer ticketId) {
        if(ticketRepository.findById(ticketId).isEmpty() ) {
            //todo throw Exception Not Found
        }

        return historyListMapper.toHistoryResponseDtoList(
                historyRepository.findAllByTicketId(ticketId)
        );
    }

    @Override
    public void delete(Integer id) {
        if(historyRepository.findById(id).isEmpty() ) {
            //todo throw Exception Not Found
        }

        historyRepository.delete(id);
    }
}
