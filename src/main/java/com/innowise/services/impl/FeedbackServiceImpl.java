package com.innowise.services.impl;

import com.innowise.exceptions.EntityTypeMessages;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.exceptions.TicketHasFeedbackException;
import com.innowise.mails.EmailService;
import com.innowise.util.mappers.FeedbackListMapper;
import com.innowise.util.mappers.FeedbackMapper;
import com.innowise.dto.request.FeedbackRequest;
import com.innowise.dto.response.FeedbackResponse;
import com.innowise.domain.Feedback;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.exceptions.NotOwnerTicketException;
import com.innowise.exceptions.TicketNotDoneException;
import com.innowise.repositories.FeedbackRepository;
import com.innowise.services.FeedbackService;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackListMapper feedbackListMapper;
    private final UserService userService;
    @Lazy private final TicketService ticketService;
    private final EmailService emailService;

    @Override
    @PreAuthorize(value = "hasAnyRole('MANAGER', 'EMPLOYEE')")
    @Validated
    public FeedbackResponse save(@Valid FeedbackRequest feedbackRequest, String contextUserName) {
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);

        Ticket ticket = ticketService.findByIdService(feedbackRequest.ticketId()).orElseThrow(() ->
                new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, feedbackRequest.ticketId()));
        User owner = userService.findByEmailService(contextUserName);

        if (!ticket.getState().equals(TicketState.DONE)) {
            throw new TicketNotDoneException(ticket.getId());
        }
        if (!ticket.getOwner().getId().equals(owner.getId())) {
            throw new NotOwnerTicketException(owner.getId(), ticket.getId());
        }
        if (!(ticket.getFeedback() == null)) {
            throw new TicketHasFeedbackException(ticket.getId());
        }
        emailService.notifyFeedbackProvide(ticket.getAssignee(), ticket.getId());

        feedback.setId(ticket.getId());
        feedback.setUser(owner);
        feedback.setTicket(ticket);
        feedback.setDate(LocalDate.now());

        return feedbackMapper.toFeedbackResponseDto(feedbackRepository.save(feedback));
    }

    @Override
    public List<FeedbackResponse> findAll() {
        return feedbackListMapper.toFeedbackResponseDtoList(feedbackRepository.findAll());
    }

    @Override
    public void delete(Integer id) {
        if (feedbackRepository.existsById(id)) {
            feedbackRepository.delete(id);
        } else {
            throw new NoSuchEntityIdException(EntityTypeMessages.FEEDBACK_MESSAGE, id);
        }
    }

    @Override
    public FeedbackResponse findById(Integer id) {
        return feedbackMapper.toFeedbackResponseDto(feedbackRepository.findById(id).orElseThrow(() ->
                new NoSuchEntityIdException(EntityTypeMessages.FEEDBACK_MESSAGE, id)));
    }

    @Override
    public FeedbackResponse findByTicketId(Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            if(feedbackRepository.existsById(ticketId)){
                return feedbackMapper.toFeedbackResponseDto(feedbackRepository.findByTicketId(ticketId));
            } else {
                throw new NoSuchEntityIdException(EntityTypeMessages.FEEDBACK_MESSAGE, ticketId);
            }
        } else {
            throw new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, ticketId);
        }
    }
}
