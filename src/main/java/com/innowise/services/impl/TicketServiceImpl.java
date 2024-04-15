package com.innowise.services.impl;

import com.innowise.domain.Ticket;
import com.innowise.domain.Attachment;
import com.innowise.domain.Category;
import com.innowise.domain.History;
import com.innowise.domain.User;
import com.innowise.domain.enums.UserRole;
import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.response.AttachmentResponse;
import com.innowise.exceptions.AttachedFileReadException;
import com.innowise.exceptions.EntityTypeMessages;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.exceptions.NotOwnerTicketException;
import com.innowise.exceptions.TicketStateTransferException;
import com.innowise.mails.EmailService;
import com.innowise.services.AttachmentService;
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
    private final AttachmentService attachmentService;
    private final CategoryService categoryService;
    private final EmailService emailService;
    private final MimeDetector mimeDetector;
    private final String API_CONTEXT_PATH;
    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper,
                             TicketListMapper ticketListMapper, UserService userService, AttachmentService attachmentService,
                             CategoryService categoryService, EmailService emailService, MimeDetector mimeDetector,
                             @Value("${api.context.path}") String API_CONTEXT_PATH) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketListMapper = ticketListMapper;
        this.userService = userService;
        this.attachmentService = attachmentService;
        this.categoryService = categoryService;
        this.emailService = emailService;
        this.mimeDetector = mimeDetector;
        this.API_CONTEXT_PATH = API_CONTEXT_PATH;
    }

    @Override
    @PreAuthorize(value = "hasAnyRole('MANAGER', 'EMPLOYEE')")
    @Validated
    public TicketResponse save(CreateTicketRequest request) {
        Category category = categoryService.findById(request.categoryId());
        User owner = userService.getUserFromPrincipal();
        List<History> history = new ArrayList<>();
        TicketState state = request.state();
        Ticket ticket = Ticket.builder()
                .name(request.name())
                .createdOn(LocalDate.now())
                .desiredResolutionDate(request.desiredResolutionDate())
                .description(request.description())
                .category(category)
                .owner(owner)
                .urgency(request.urgency())
                .state(state)
                .build();

        List<Attachment> content = toAttachmentList(ticket, request.files());
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
                .toTicketResponseDtoList(ticketRepository.findAllByAssigneeEmail(email));
    }

    @Override
    @PreAuthorize(value = "hasAnyRole('MANAGER', 'EMPLOYEE')")
    @Validated
    public TicketResponse update(@Valid UpdateTicketRequest request) {
        Ticket ticket = ticketRepository.findById(request.id())
                .orElseThrow(() ->
                        new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, request.id()));
        if (!ticket.getState().equals(TicketState.DRAFT)) {
            throw new TicketNotDraftException(request.id());
        }
        User updatedBy = userService.getUserFromPrincipal();
        if (!ticket.getOwner().equals(updatedBy)) {
            throw new NotOwnerTicketException(updatedBy.getId(), ticket.getId());
        }

        List<Attachment> content = toAttachmentList(ticket, request.files());
        ticket.getAttachments().clear();
        attachmentService.replaceAttachmentsByTicketId(request.id(), content);
        ticket.setName(request.name());
        ticket.setDescription(request.description());
        ticket.setUrgency(request.urgency());
        ticket.setDesiredResolutionDate(request.desiredResolutionDate());
        ticket.setCategory(categoryService.findById(request.categoryId()));
        ticket.getHistories().add(History.ofUpdate(updatedBy, ticket));
        Ticket savedTicket = ticketRepository.update(ticket);
        return toTicketResponse(savedTicket);
    }


    @Override
    @Validated
    public TicketResponse updateStatus(@Valid UpdateTicketStatusRequest updateTicketStatusRequest) {
        Ticket ticket = ticketRepository.findById(updateTicketStatusRequest.ticketId()).orElseThrow(
                () -> new NoSuchEntityIdException(
                        EntityTypeMessages.TICKET_MESSAGE,
                        updateTicketStatusRequest.ticketId()));
        User editor = userService.getUserFromPrincipal();
        if(!checkStatusChangeAuthorities(editor, ticket, updateTicketStatusRequest)) {
            throw new TicketStateTransferException(ticket.getId(), editor.getRole(), ticket.getState());
        }
        if(updateTicketStatusRequest.state().equals(TicketState.IN_PROGRESS)) {
            ticket.setAssignee(editor);
            // TODO tests for TicketService *to ycovich & NomedXD*
            // UPD method doesn't work at all :)
        }
        emailService.notifyTicketStateTransfer(ticket.getState(), ticket, updateTicketStatusRequest.state());
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

    private boolean checkStatusChangeAuthorities(User editor, Ticket ticket, UpdateTicketStatusRequest updateTicketStatusRequest) {
        if(editor.getRole().getFromToStateAuthorities().containsKey(ticket.getState())) {
            List<TicketState> ticketTransferStatesList = editor.getRole().getFromToStateAuthorities().get(ticket.getState());
            if (editor.getRole().equals(UserRole.ROLE_MANAGER)) {
                switch (ticket.getState()) {
                    case DRAFT, DECLINED -> {
                        return ticketTransferStatesList.contains(updateTicketStatusRequest.state()) && ticket.getOwner().getId().equals(editor.getId());
                    }
                    case NEW -> {
                        return ticketTransferStatesList.contains(updateTicketStatusRequest.state());
                    }
                }
            } else {
                return ticketTransferStatesList.contains(updateTicketStatusRequest.state());
            }
        }
        return false;
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

    private List<Attachment> toAttachmentList(Ticket ticket, MultipartFile[] files) {
        List<Attachment> content = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
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
        return content;
    }
}
