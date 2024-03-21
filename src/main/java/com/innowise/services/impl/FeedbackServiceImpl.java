package com.innowise.services.impl;

import com.innowise.dto.mappers.FeedbackListMapper;
import com.innowise.dto.mappers.FeedbackMapper;
import com.innowise.dto.requestDto.FeedbackRequestDto;
import com.innowise.dto.responseDto.FeedbackResponseDto;
import com.innowise.domain.Feedback;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.exceptions.NoSuchFeedbackException;
import com.innowise.exceptions.NoSuchTicketException;
import com.innowise.exceptions.NoSuchUserIdException;
import com.innowise.exceptions.NotOwnerTicketException;
import com.innowise.exceptions.TicketNotDoneException;
import com.innowise.repositories.FeedbackRepository;
import com.innowise.services.FeedbackService;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackListMapper feedbackListMapper;
    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               FeedbackMapper feedbackMapper, FeedbackListMapper feedbackListMapper, TicketService ticketService, UserService userService) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.feedbackListMapper = feedbackListMapper;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @Override
    public FeedbackResponseDto save(FeedbackRequestDto feedbackRequestDto) {
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequestDto);

        Ticket ticket = ticketService.findByIdService(feedbackRequestDto.ticketId()).orElseThrow(() -> new NoSuchTicketException(feedbackRequestDto.ticketId()));
        User owner = userService.findByIdService(feedbackRequestDto.userId()).orElseThrow(() -> new NoSuchUserIdException(feedbackRequestDto.userId()));

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
    public List<FeedbackResponseDto> findAll() {
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
    public FeedbackResponseDto findById(Integer id) {
        return feedbackMapper.toFeedbackResponseDto(feedbackRepository.findById(id).orElseThrow(() -> new NoSuchFeedbackException(id)));
    }

    @Override
    public List<FeedbackResponseDto> findAllByTicketId(Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            return feedbackListMapper.toFeedbackResponseDtoList(feedbackRepository.findAllByTicketId(ticketId));
        } else {
            throw new NoSuchTicketException(ticketId);
        }
    }
}
