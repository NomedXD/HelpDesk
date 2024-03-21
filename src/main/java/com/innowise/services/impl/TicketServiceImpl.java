package com.innowise.services.impl;

import com.innowise.util.mappers.TicketListMapper;
import com.innowise.util.mappers.TicketMapper;
import com.innowise.dto.requestDto.TicketChangeStatusRequestDto;
import com.innowise.dto.requestDto.TicketRequestDto;
import com.innowise.dto.responseDto.TicketResponseDto;
import com.innowise.domain.History;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.exceptions.NoSuchCategoryException;
import com.innowise.exceptions.NoSuchTicketException;
import com.innowise.exceptions.NoSuchUserIdException;
import com.innowise.exceptions.NotOwnerTicketException;
import com.innowise.exceptions.TicketNotDraftException;
import com.innowise.repositories.TicketRepository;
import com.innowise.services.CategoryService;
import com.innowise.services.CommentService;
import com.innowise.services.HistoryService;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import com.innowise.util.HistoryBuilder;
import com.innowise.util.HistoryCreationOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketListMapper ticketListMapper;
    private final CommentService commentService;
    private final HistoryService historyService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TicketListMapper ticketListMapper, CommentService commentService, HistoryService historyService, UserService userService, CategoryService categoryService, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketListMapper = ticketListMapper;
        this.commentService = commentService;
        this.historyService = historyService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.ticketMapper = ticketMapper;

    }

    @Override
    public TicketResponseDto save(TicketRequestDto ticketRequestDto) {
        Ticket ticket = ticketMapper.toTicket(ticketRequestDto);

        ticket.setCategory(categoryService.findByIdService(ticketRequestDto.categoryId()).orElseThrow(() -> new NoSuchCategoryException(ticketRequestDto.categoryId())));
        Optional<User> owner = userService.findByIdService(ticketRequestDto.creatorId());
        ticket.setOwner(owner.orElseThrow(() -> new NoSuchUserIdException(ticketRequestDto.creatorId())));
        ticket.setCreatedOn(LocalDate.now());

        if (ticketRequestDto.comment() != null && !ticketRequestDto.comment().isBlank()) {
            ticket.setComments(List.of(commentService.saveByTicket
                    (owner.get(), ticketRequestDto.comment(), ticketRequestDto.id())));
        } else {
            ticket.setComments(new ArrayList<>());
        }
        //todo save files (Attachments) Usovich ticketToSave.setAttachments();

        ticket.setHistories(List.of(historyService.saveService(HistoryBuilder.buildHistory(owner.get(), ticket, HistoryCreationOption.CREATE_TICKET))));
        return ticketMapper.toTicketResponseDto(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDto findById(Integer id) {
        return ticketMapper.toTicketResponseDto(
                ticketRepository.findById(id).orElseThrow(() -> new NoSuchTicketException(id)));
    }

    @Override
    public List<TicketResponseDto> findAll() {
        return ticketListMapper.toTicketResponseDtoList(ticketRepository.findAll());
    }

    @Override
    public TicketResponseDto update(TicketRequestDto ticketRequestDto) {
        Ticket ticket = ticketRepository.findById(ticketRequestDto.id()).orElseThrow(() -> new NoSuchTicketException(ticketRequestDto.id()));

        if (!ticket.getState().equals(TicketState.DRAFT)) {
            throw new TicketNotDraftException(ticketRequestDto.id());
        }

        ticket.setName(ticketRequestDto.name());
        ticket.setDescription(ticketRequestDto.description());
        ticket.setUrgency(ticketRequestDto.urgency());
        ticket.setDesiredResolutionDate(ticketRequestDto.desiredResolutionDate());

        ticket.setCategory(categoryService.findByIdService(ticketRequestDto.categoryId()).orElseThrow(
                () -> new NoSuchCategoryException(ticketRequestDto.categoryId())));

        User owner = userService.findByIdService(ticketRequestDto.creatorId()).orElseThrow(() -> new NoSuchUserIdException(ticketRequestDto.creatorId()));
        if (!owner.getId().equals(ticket.getOwner().getId())) {
            throw new NotOwnerTicketException(owner.getId(), ticket.getId());
        }
        ticket.setOwner(owner);
        // Задел на будущее, если все таки будут каскады
        ticket.getHistories().add(historyService.saveService(HistoryBuilder.buildHistory(owner, ticket, HistoryCreationOption.EDIT_TICKET)));

        return ticketMapper.toTicketResponseDto(ticketRepository.update(ticket));
    }

    @Override
    public TicketResponseDto updateStatus(TicketChangeStatusRequestDto ticketChangeStatusRequestDto) {
        Ticket ticket = ticketRepository.findById(ticketChangeStatusRequestDto.ticketId()).orElseThrow(
                () -> new NoSuchTicketException(ticketChangeStatusRequestDto.ticketId()));
        History history = HistoryBuilder.buildHistory(ticket.getOwner(), ticket, ticket.getState(), ticketChangeStatusRequestDto.state());
        ticket.setState(ticketChangeStatusRequestDto.state());
        ticket.getHistories().add(historyService.saveService(history));

        return ticketMapper.toTicketResponseDto(ticketRepository.update(ticket));
    }

    @Override
    public void delete(Integer id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.delete(id);
        } else {
            throw new NoSuchTicketException(id);
        }
    }

    @Override
    public Optional<Ticket> findByIdService(Integer id) {
        return ticketRepository.findById(id);
    }

    @Override
    public boolean existsByIdService(Integer id) {
        return ticketRepository.existsById(id);
    }
}
