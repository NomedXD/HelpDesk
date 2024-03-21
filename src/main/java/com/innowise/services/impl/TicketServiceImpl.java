package com.innowise.services.impl;

import com.innowise.domain.Attachment;
import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.util.mappers.TicketListMapper;
import com.innowise.util.mappers.TicketMapper;
import com.innowise.dto.request.ChangeTicketStatusRequest;
import com.innowise.dto.request.UpdateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.domain.History;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.exceptions.NoSuchCategoryException;
import com.innowise.exceptions.NoSuchTicketException;
import com.innowise.exceptions.NoSuchUserIdException;
import com.innowise.exceptions.TicketNotDraftException;
import com.innowise.repositories.TicketRepository;
import com.innowise.services.CategoryService;
import com.innowise.services.CommentService;
import com.innowise.services.HistoryService;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import com.innowise.util.HistoryBuilder;
import com.innowise.util.HistoryCreationOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
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

    @Override
    public TicketResponse save(CreateTicketRequest request) throws IOException {
        Ticket ticket = ticketMapper.toTicket(request);
        ticket.setCreatedOn(LocalDate.now());
        ticket.setCategory(categoryService
                .findByIdService(request.categoryId()).orElseThrow(() ->
                        new NoSuchCategoryException(request.categoryId())));
        Optional<User> owner = userService
                .findByIdService(request.ownerId());
        ticket.setOwner(owner.orElseThrow(() ->
                new NoSuchUserIdException(request.ownerId())));

        if (request.comment() != null && !request.comment().isBlank()) {
            ticket.setComments(List.of(commentService.saveByTicket
                    (owner.get(), request.comment(), ticket.getId())));
            // TODO discuss problem of returning DTO from save method (by ycovich)
        } else {
            ticket.setComments(new ArrayList<>());
        }

        List<Attachment> content = new ArrayList<>();
        for (MultipartFile file : request.files()) {
            Attachment attachment = new Attachment();
            attachment.setTicketId(ticket.getId());
            attachment.setName(file.getOriginalFilename());
            attachment.setBlob(file.getBytes());
            content.add(attachment);
        }
        ticket.setAttachments(content);

        ticket.setHistories(List.of(historyService
                .saveService(HistoryBuilder
                        .buildHistory(owner.get(), ticket, HistoryCreationOption.CREATE_TICKET))));
        return ticketMapper.toTicketResponseDto(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponse findById(Integer id) {
        return ticketMapper.toTicketResponseDto(
                ticketRepository.findById(id).orElseThrow(() -> new NoSuchTicketException(id)));
    }

    @Override
    public List<TicketResponse> findAll() {
        return ticketListMapper.toTicketResponseDtoList(ticketRepository.findAll());
    }

    @Override
    public TicketResponse update(UpdateTicketRequest updateTicketRequest) {
        Ticket ticket = ticketRepository.findById(updateTicketRequest.id()).orElseThrow(() -> new NoSuchTicketException(updateTicketRequest.id()));

        if (!ticket.getState().equals(TicketState.DRAFT)) {
            throw new TicketNotDraftException(updateTicketRequest.id());
        }

        ticket.setName(updateTicketRequest.name());
        ticket.setDescription(updateTicketRequest.description());
        ticket.setUrgency(updateTicketRequest.urgency());
        ticket.setDesiredResolutionDate(updateTicketRequest.desiredResolutionDate());

        ticket.setCategory(categoryService.findByIdService(updateTicketRequest.categoryId()).orElseThrow(
                () -> new NoSuchCategoryException(updateTicketRequest.categoryId())));

//        User owner = userService.findByIdService(updateTicketRequest.ownerId()).orElseThrow(() -> new NoSuchUserIdException(updateTicketRequest.ownerId()));
//        if (!owner.getId().equals(ticket.getOwner().getId())) {
//            throw new NotOwnerTicketException(owner.getId(), ticket.getId());
//        }
//        ticket.setOwner(owner);
        // TODO ^ strange approach (IMHO ycovich) ^
        // TODO anyway owner will be retrieved from principal/authentication

        // Задел на будущее, если все таки будут каскады
        //ticket.getHistories().add(historyService.saveService(HistoryBuilder.buildHistory(owner, ticket, HistoryCreationOption.EDIT_TICKET)));

        return ticketMapper.toTicketResponseDto(ticketRepository.update(ticket));
    }

    @Override
    public TicketResponse updateStatus(ChangeTicketStatusRequest changeTicketStatusRequest) {
        Ticket ticket = ticketRepository.findById(changeTicketStatusRequest.ticketId()).orElseThrow(
                () -> new NoSuchTicketException(changeTicketStatusRequest.ticketId()));
        History history = HistoryBuilder.buildHistory(ticket.getOwner(), ticket, ticket.getState(), changeTicketStatusRequest.state());
        ticket.setState(changeTicketStatusRequest.state());
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
    // TODO discuss problem of returning DTO from save method (by ycovich)

    @Override
    public boolean existsByIdService(Integer id) {
        return ticketRepository.existsById(id);
    }
}
