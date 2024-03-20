package com.innowise.service.impl;

import com.innowise.controller.dto.mapper.TicketMapper;
import com.innowise.controller.dto.requestDto.TicketChangeStatusRequestDto;
import com.innowise.controller.dto.requestDto.TicketRequestDto;
import com.innowise.controller.dto.responseDto.TicketResponseDto;
import com.innowise.domain.Comment;
import com.innowise.domain.History;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.repo.CategoryRepository;
import com.innowise.repo.HistoryRepository;
import com.innowise.repo.TicketRepository;
import com.innowise.repo.UserRepository;
import com.innowise.service.TicketService;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    //private final TicketListMapper ticketListMapper;

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final HistoryRepository historyRepository;

    @PersistenceContext
    private Session session;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository, CategoryRepository categoryRepository, TicketMapper ticketMapper, HistoryRepository historyRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.ticketMapper = ticketMapper;
        this.historyRepository = historyRepository;
    }

    @Override
    public TicketResponseDto findById(Integer id) {
        return ticketMapper.toTicketResponseDto(
                ticketRepository.findById(id).orElseThrow(
                        //todo throw Entity Not Found
                )
        );
    }

    //todo сделать TicketListMapper
    @Override
    public List<TicketResponseDto> findAll() {
        return ticketRepository.findAll()
                .stream().map(ticketMapper::toTicketResponseDto)
                .toList();
    }

    @Override
    public TicketResponseDto save(TicketRequestDto ticket) {
        if(ticketRepository.findById(ticket.id()).isPresent()) {
            //todo throw Exception if ticker with this ID already present
        }

        Ticket ticketToSave = ticketMapper.toTicket(ticket);
        User owner = userRepository.findById(ticket.creatorId()).orElseThrow(
                //todo throw Entity Not Found
        );

        ticketToSave.setCategory(categoryRepository.findById(ticket.categoryId()).orElseThrow(
                //todo throw Entity Not Found
        ));
        ticketToSave.setOwner(owner);
        ticketToSave.setCreatedOn(LocalDate.now());

        session.persist(ticketToSave);

        if(ticket.comment() != null && !ticket.comment().isBlank()) {
            Comment comment = new Comment();
            comment.setDate(LocalDateTime.now());
            comment.setUser(owner);
            comment.setText(ticket.comment());

            comment.setTicketId(ticketToSave.getId());
            ticketToSave.setComments(List.of(comment));

            session.persist(comment);
        }

        //todo save files (Attachments) Usovich ticketToSave.setAttachments();
        //when file attached or removed from ticket, new History object

        History history = History.builder()
                .description("Ticket is created")
                .action("Ticket is created")
                .date(LocalDateTime.now())
                .user(owner)
                .ticket(ticketToSave)
                .build();
        session.persist(history);
        ticketToSave.setHistories(List.of(history));

        return ticketMapper.toTicketResponseDto(ticketToSave);
    }

    @Override
    public TicketResponseDto update(TicketRequestDto ticket) {
        Ticket ticketToUpdate = ticketRepository.findById(ticket.id()).orElseThrow(
                //todo throw Exception Not Found
        );

        if(!ticketToUpdate.getState().equals(TicketState.DRAFT)) {
            //todo throw Exception ticket in wrong status to update
        }

        ticketToUpdate.setName(ticket.name());
        ticketToUpdate.setDescription(ticket.description());
        ticketToUpdate.setUrgency(ticket.urgency());

        ticketToUpdate.setCategory(categoryRepository.findById(ticket.categoryId()).orElseThrow(
                //todo throw Entity Not Found
        ));

        User editor = userRepository.findById(ticket.creatorId()).orElseThrow(
                //todo throw Exception Not Found
        );

        History history = History.builder()
                .description("Ticket is edited")
                .action("Ticket is edited")
                .date(LocalDateTime.now())
                .user(editor)
                .ticket(ticketToUpdate)
                .build();
        historyRepository.save(history);

        return ticketMapper.toTicketResponseDto(
                ticketRepository.update(ticketToUpdate)
        );
    }

    @Override
    public TicketResponseDto updateStatus(TicketChangeStatusRequestDto changeStatusDto) {
        Ticket ticketToUpdate = ticketRepository.findById(changeStatusDto.ticketId()).orElseThrow(
                //todo throw Entity Not Found
        );
        TicketState lastState = ticketToUpdate.getState();
        ticketToUpdate.setState(changeStatusDto.state());

        History history = History.builder()
                .description("Ticket status is changed from %s to %s"
                        .formatted(lastState, changeStatusDto.state()))
                .action("Ticket is edited")
                .date(LocalDateTime.now())
                .user(editor) //todo we need to know who changed status, maybe add field editor in dto
                .ticket(ticketToUpdate)
                .build();
        historyRepository.save(history);

        return ticketMapper.toTicketResponseDto(
                ticketRepository.update(ticketToUpdate)
        );
    }

    @Override
    public void delete(Integer id) {
        ticketRepository.delete(id);
    }
}
