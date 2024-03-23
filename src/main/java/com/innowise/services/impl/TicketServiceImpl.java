package com.innowise.services.impl;

import com.innowise.domain.Ticket;
import com.innowise.domain.Attachment;
import com.innowise.domain.Comment;
import com.innowise.domain.History;
import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.util.mappers.TicketListMapper;
import com.innowise.util.mappers.TicketMapper;
import com.innowise.dto.request.ChangeTicketStatusRequest;
import com.innowise.dto.request.UpdateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.domain.enums.TicketState;
import com.innowise.exceptions.NoSuchTicketException;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Validated
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketListMapper ticketListMapper;
    private final CommentService commentService;
    private final HistoryService historyService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    @Validated
    public TicketResponse save(CreateTicketRequest request) throws IOException {
        var category = categoryService.findById(request.categoryId());
        var owner = userService.findById(request.ownerId());
        List<Comment> comments = new ArrayList<>();
        List<Attachment> content = new ArrayList<>();
        List<History> history = new ArrayList<>();

        Ticket ticket = Ticket.builder()
                .name(request.name())
                .createdOn(LocalDate.now())
                .desiredResolutionDate(request.desiredResolutionDate())
                .description(request.description())
                .category(category)
                .owner(owner)
                .urgency(request.urgency())
                .build();
        Ticket persistedTicket = ticketRepository.save(ticket);

        // This is the point when we can retrieve ticket ID from database and can proceed with other fields
        if (request.comment() != null && !request.comment().isBlank()) {
            Comment comment = Comment.builder()
                    .date(LocalDateTime.now())
                    .user(owner)
                    .text(request.comment())
                    .build();
            comments.add(comment);
        }
        if (request.files() != null) {
            for (MultipartFile file : request.files()) {
                Attachment attachment = new Attachment();
                attachment.setTicketId(persistedTicket.getId());
                attachment.setName(file.getOriginalFilename());
                attachment.setBlob(file.getBytes());
                content.add(attachment);
            }
        }
        history.add(HistoryBuilder
                .buildHistory(owner, persistedTicket, HistoryCreationOption.CREATE_TICKET));

        persistedTicket.setComments(comments);
        persistedTicket.setAttachments(content);
        persistedTicket.setHistories(history);

        return ticketMapper.toTicketResponseDto(ticketRepository.save(persistedTicket));
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
    @Validated
    public TicketResponse update(UpdateTicketRequest updateTicketRequest) {
        Ticket ticket = ticketRepository.findById(updateTicketRequest.id()).orElseThrow(() -> new NoSuchTicketException(updateTicketRequest.id()));

        if (!ticket.getState().equals(TicketState.DRAFT)) {
            throw new TicketNotDraftException(updateTicketRequest.id());
        }

        ticket.setName(updateTicketRequest.name());
        ticket.setDescription(updateTicketRequest.description());
        ticket.setUrgency(updateTicketRequest.urgency());
        ticket.setDesiredResolutionDate(updateTicketRequest.desiredResolutionDate());

        ticket.setCategory(categoryService.findById(updateTicketRequest.categoryId()));

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
    @Validated
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

    @Override
    public boolean existsByIdService(Integer id) {
        return ticketRepository.existsById(id);
    }
}
