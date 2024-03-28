package com.innowise.services.impl;

import com.innowise.domain.Ticket;
import com.innowise.domain.Attachment;
import com.innowise.domain.Category;
import com.innowise.domain.History;
import com.innowise.domain.User;
import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.response.AttachmentResponse;
import com.innowise.exceptions.AttachedFileReadException;
import com.innowise.exceptions.EntityTypeMessages;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.exceptions.NotOwnerTicketException;
import com.innowise.util.MimeDetector;
import com.innowise.util.mappers.TicketListMapper;
import com.innowise.util.mappers.TicketMapper;
import com.innowise.dto.request.UpdateTicketStatusRequest;
import com.innowise.dto.request.UpdateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.domain.enums.TicketState;
import com.innowise.exceptions.TicketNotDraftException;
import com.innowise.repositories.TicketRepository;
import com.innowise.services.CategoryService;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@Validated
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketListMapper ticketListMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final MimeDetector mimeDetector;
    private final String API_CONTEXT_PATH;
    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper,
                             TicketListMapper ticketListMapper, UserService userService,
                             CategoryService categoryService, MimeDetector mimeDetector,
                             @Value("${api.context.path}") String API_CONTEXT_PATH) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketListMapper = ticketListMapper;
        this.userService = userService;
        this.categoryService = categoryService;
        this.mimeDetector = mimeDetector;
        this.API_CONTEXT_PATH = API_CONTEXT_PATH;
    }

    @Override
    @PreAuthorize(value = "hasAnyRole('MANAGER', 'EMPLOYEE')")
    @Validated
    public TicketResponse save(CreateTicketRequest request) {
        Category category = categoryService.findById(request.categoryId());
        User owner = userService.getUserFromPrincipal();
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
                .state(TicketState.NEW)
                .build();

        if (request.files() != null) {
            for (MultipartFile file : request.files()) {
                try {
                    content.add(Attachment.builder()
                            .name(file.getOriginalFilename())
                            .blob(file.getBytes())
                            .ticket(ticket)
                            .build());
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    throw new AttachedFileReadException(file.getOriginalFilename(), e.getMessage());
                }
            }
        }
        ticket.setAttachments(content);
        history.add(History.ofCreate(owner, ticket));
        ticket.setHistories(history);

        Ticket savedTicket = ticketRepository.save(ticket);
        return toTicketResponse(savedTicket);
    }

    @Override
    public TicketResponse findById(Integer id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, id));
        return toTicketResponse(ticket);
    }

    @Override
    public List<TicketResponse> findAll() {
        return ticketListMapper.toTicketResponseDtoList(ticketRepository.findAll());
    }

    @Override
    public List<TicketResponse> findAllByAssigneeEmail(String email) {
        return ticketListMapper
                .toTicketResponseDtoList(ticketRepository
                        .findAllByAssigneeId(userService
                                .findByEmail(email)
                                .id()));
    }

    @Override
    @PreAuthorize(value = "hasAnyRole('MANAGER', 'EMPLOYEE')")
    @Validated
    public TicketResponse update(@Valid UpdateTicketRequest updateTicketRequest) {
        Ticket ticket = ticketRepository.findById(updateTicketRequest.id())
                .orElseThrow(() ->
                        new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, updateTicketRequest.id()));
        if (!ticket.getState().equals(TicketState.DRAFT)) {
            throw new TicketNotDraftException(updateTicketRequest.id());
        }
        User updatedBy = userService.getUserFromPrincipal();
        if (!ticket.getOwner().equals(updatedBy)) {
            throw new NotOwnerTicketException(updatedBy.getId(), ticket.getId());
        }

        ticket.setName(updateTicketRequest.name());
        ticket.setDescription(updateTicketRequest.description());
        ticket.setUrgency(updateTicketRequest.urgency());
        ticket.setDesiredResolutionDate(updateTicketRequest.desiredResolutionDate());
        ticket.setCategory(categoryService.findById(updateTicketRequest.categoryId()));
        ticket.getHistories().add(History.ofUpdate(updatedBy, ticket));
        Ticket savedTicket = ticketRepository.update(ticket);
        return toTicketResponse(savedTicket);
    }


    // TODO add checks of editor role for possible status transition
    @Override
    @Validated
    public TicketResponse updateStatus(@Valid UpdateTicketStatusRequest updateTicketStatusRequest) {
        Ticket ticket = ticketRepository.findById(updateTicketStatusRequest.ticketId()).orElseThrow(
                () -> new NoSuchEntityIdException(
                        EntityTypeMessages.TICKET_MESSAGE,
                        updateTicketStatusRequest.ticketId()));
        User editor = userService.getUserFromPrincipal();

        ticket.setState(updateTicketStatusRequest.state());
        ticket.getHistories()
                .add(History.ofStatusChange(
                        ticket.getState(),
                        updateTicketStatusRequest.state(),
                        editor,
                        ticket));
        Ticket savedTicket = ticketRepository.update(ticket);
        return toTicketResponse(savedTicket);
    }

    @Override
    public void delete(Integer id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.delete(id);
        } else {
            throw new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, id);
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

    // TODO make this a separate class and move to util package *NOT URGENT*
    private TicketResponse toTicketResponse(Ticket ticket) {
        TicketResponse response = ticketMapper.toTicketResponseDto(ticket);
        List<Attachment> attachments = ticket.getAttachments();
        List<AttachmentResponse> attachmentResponseList = new ArrayList<>();
        for (Attachment attachment : attachments) {
            String mimeType = mimeDetector.deetectMimeType(attachment.getBlob());
            AttachmentResponse attachmentResponse = AttachmentResponse.builder()
                    .id(attachment.getId())
                    .name(attachment.getName())
                    .url(API_CONTEXT_PATH + "/attachments/" + attachment.getId())
                    .type(mimeType)
                    .size((long) attachment.getBlob().length)
                    .build();
            attachmentResponseList.add(attachmentResponse);
        }
        response.setAttachments(attachmentResponseList);
        return response;
    }
}
