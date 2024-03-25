package com.innowise.services.impl;

import com.innowise.util.mappers.FeedbackListMapper;
import com.innowise.util.mappers.FeedbackMapper;
import com.innowise.dto.request.FeedbackRequest;
import com.innowise.dto.response.FeedbackResponse;
import com.innowise.domain.Feedback;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.exceptions.NoSuchFeedbackException;
import com.innowise.exceptions.NoSuchTicketException;
import com.innowise.exceptions.NotOwnerTicketException;
import com.innowise.exceptions.TicketNotDoneException;
import com.innowise.repositories.FeedbackRepository;
import com.innowise.services.FeedbackService;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Validated
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackListMapper feedbackListMapper;
    private final UserService userService;
    private TicketService ticketService;

    @Override
    @Validated
    public FeedbackResponse save(FeedbackRequest feedbackRequest) {
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);

        Ticket ticket = ticketService.findByIdService(feedbackRequest.ticketId()).orElseThrow(() -> new NoSuchTicketException(feedbackRequest.ticketId()));
        User owner = userService.findById(feedbackRequest.userId());

        // todo На фронте тоже отключить кнопку для не DONE тикетов
        if (!ticket.getState().equals(TicketState.DONE)) {
            throw new TicketNotDoneException(ticket.getId());
        }
        if (!ticket.getOwner().getId().equals(owner.getId())) {
            throw new NotOwnerTicketException(owner.getId(), ticket.getId());
        }

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
            throw new NoSuchFeedbackException(id);
        }
    }

    @Override
    public FeedbackResponse findById(Integer id) {
        return feedbackMapper.toFeedbackResponseDto(feedbackRepository.findById(id).orElseThrow(() -> new NoSuchFeedbackException(id)));
    }

    @Override
    public List<FeedbackResponse> findAllByTicketId(Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            return feedbackListMapper.toFeedbackResponseDtoList(feedbackRepository.findAllByTicketId(ticketId));
        } else {
            throw new NoSuchTicketException(ticketId);
        }
    }

    @Autowired
    @Lazy
    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }
}
