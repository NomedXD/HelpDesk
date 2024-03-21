package com.innowise.service.impl;

import com.innowise.controller.dto.mapper.FeedbackMapper;
import com.innowise.controller.dto.requestDto.FeedbackRequestDto;
import com.innowise.controller.dto.responseDto.FeedbackResponseDto;
import com.innowise.domain.Feedback;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.repo.FeedbackRepository;
import com.innowise.repo.TicketRepository;
import com.innowise.repo.UserRepository;
import com.innowise.service.FeedbackService;
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

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               FeedbackMapper feedbackMapper, TicketRepository ticketRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FeedbackResponseDto findById(Integer id) {
        return feedbackMapper.toFeedbackResponseDto(feedbackRepository.findById(id).orElseThrow(
                //todo throw Exception Not Found
        ));
    }

    @Override
    public List<FeedbackResponseDto> findAll() {
        //todo feedback list mapper
        return feedbackRepository.findAll().stream()
                .map(feedbackMapper::toFeedbackResponseDto)
                .toList();
    }

    @Override
    public List<FeedbackResponseDto> findAllByTicketId(Integer ticketId) {
        if(ticketRepository.findById(ticketId).isEmpty()) {
            //todo throw Exception Not Found
        }

        return feedbackRepository.findAllByTicketId(ticketId).stream()
                .map(feedbackMapper::toFeedbackResponseDto)
                .toList();
    }

    @Override
    public FeedbackResponseDto save(FeedbackRequestDto feedback) {
        Feedback feedbackToSave = feedbackMapper.toFeedback(feedback);

        Ticket ticket = ticketRepository.findById(feedback.ticketId()).orElseThrow(
                //todo throw Exception Not Found
        );
        User owner = userRepository.findById(feedback.userId()).orElseThrow(
                //todo throw Exception Not Found
        );

        if(!ticket.getState().equals(TicketState.DONE)) {
            //todo throw ticket in wrong state to feedback Exception
        }
        if(!ticket.getOwner().equals(owner)) {
            //todo throw only owner can leave feedback Exception
        }

        feedbackToSave.setUser(owner);
        feedbackToSave.setTicket(ticket);
        feedbackToSave.setDate(LocalDate.now());

        return feedbackMapper.toFeedbackResponseDto(feedbackRepository.save(feedbackToSave));
    }

    @Override
    public void delete(Integer id) {
        if(feedbackRepository.findById(id).isEmpty()) {
            //todo throw Exception Not Found
        }

        feedbackRepository.delete(id);
    }
}
