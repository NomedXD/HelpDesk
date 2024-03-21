package com.innowise.service.impl;

import com.innowise.domain.*;
import com.innowise.util.mapper.TicketMapper;
import com.innowise.controller.dto.request.TicketChangeStatusRequestDto;
import com.innowise.controller.dto.request.TicketRequest;
import com.innowise.controller.dto.response.TicketResponseDto;
import com.innowise.domain.enums.TicketState;
import com.innowise.repo.CategoryRepository;
import com.innowise.repo.HistoryRepository;
import com.innowise.repo.TicketRepository;
import com.innowise.repo.UserRepository;
import com.innowise.service.TicketService;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
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
    public Ticket save(TicketRequest request) throws IOException {
        Ticket ticket = ticketMapper.toTicket(request);
        User owner = userRepository.findById(request.ownerId()).orElseThrow(
                //todo throw Entity Not Found
        );

        ticket.setCategory(categoryRepository.findById(request.categoryId()).orElseThrow(
                //todo throw Entity Not Found
        ));
        ticket.setOwner(owner);
        ticket.setCreatedOn(LocalDate.now());

        session.persist(ticket);

        // Draft attachment save handling
        // TODO check if ticket.getId() == null
        List<Attachment> content = new ArrayList<>();
        for (MultipartFile file: request.files()){
            Attachment attachment = new Attachment();
            attachment.setTicketId(ticket.getId());
            attachment.setName(file.getOriginalFilename());
            attachment.setBlob(file.getBytes());
            content.add(attachment);
        }
        ticket.setAttachments(content);

        //when file attached or removed from ticket, new History object

        History history = History.builder()
                .description("Ticket is created")
                .action("Ticket is created")
                .date(LocalDateTime.now())
                .user(owner)
                .ticket(ticket)
                .build();
        session.persist(history);
        ticket.setHistories(List.of(history));

        return ticket;
    }

//    @Override
//    public TicketResponseDto update(TicketRequest ticket) {
//        Ticket ticketToUpdate = ticketRepository.findById(ticket.id()).orElseThrow(
//                //todo throw Exception Not Found
//        );
//
//        if(!ticketToUpdate.getState().equals(TicketState.DRAFT)) {
//            //todo throw Exception ticket in wrong status to update
//        }
//
//        ticketToUpdate.setName(ticket.name());
//        ticketToUpdate.setDescription(ticket.description());
//        ticketToUpdate.setUrgency(ticket.urgency());
//
//        ticketToUpdate.setCategory(categoryRepository.findById(ticket.categoryId()).orElseThrow(
//                //todo throw Entity Not Found
//        ));
//
//        User editor = userRepository.findById(ticket.ownerId()).orElseThrow(
//                //todo throw Exception Not Found
//        );
//
//        History history = History.builder()
//                .description("Ticket is edited")
//                .action("Ticket is edited")
//                .date(LocalDateTime.now())
//                .user(editor)
//                .ticket(ticketToUpdate)
//                .build();
//        historyRepository.save(history);
//
//        return ticketMapper.toTicketResponseDto(
//                ticketRepository.update(ticketToUpdate)
//        );
//    }

//    @Override
//    public TicketResponseDto updateStatus(TicketChangeStatusRequestDto changeStatusDto) {
//        Ticket ticketToUpdate = ticketRepository.findById(changeStatusDto.ticketId()).orElseThrow(
//                //todo throw Entity Not Found
//        );
//        TicketState lastState = ticketToUpdate.getState();
//        ticketToUpdate.setState(changeStatusDto.state());
//
//        History history = History.builder()
//                .description("Ticket status is changed from %s to %s"
//                        .formatted(lastState, changeStatusDto.state()))
//                .action("Ticket is edited")
//                .date(LocalDateTime.now())
//                .user(editor) //todo we need to know who changed status, maybe add field editor in dto
//                .ticket(ticketToUpdate)
//                .build();
//        historyRepository.save(history);
//
//        return ticketMapper.toTicketResponseDto(
//                ticketRepository.update(ticketToUpdate)
//        );
//    }

    @Override
    public void delete(Integer id) {
        ticketRepository.delete(id);
    }
}
