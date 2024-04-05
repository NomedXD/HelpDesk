package com.innowise.services.impl;

import com.innowise.domain.Feedback;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.domain.enums.TicketState;
import com.innowise.dto.request.FeedbackRequest;
import com.innowise.dto.response.FeedbackResponse;
import com.innowise.mails.EmailService;
import com.innowise.repositories.FeedbackRepository;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import com.innowise.util.mappers.FeedbackMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {
    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Spy
    private FeedbackMapper feedbackMapper = Mappers.getMapper(FeedbackMapper.class);

    @Mock
    private UserService userService;

    @Mock
    private TicketService ticketService;

    @Mock
    private EmailService emailService;
    @Test
    public void save_withValidTicketId_savesFeedback() {
        // FeedbackRequest
        byte feedbackRequestRate = 4;
        String feedbackRequestText = "feedbackRequestText";
        Integer feedbackRequestTicketId = 1;
        FeedbackRequest feedbackRequest = FeedbackRequest.builder().rate(feedbackRequestRate).
                text(feedbackRequestText).ticketId(feedbackRequestTicketId).build();

        // Feedback after mapping
        String contextUserName = "user@example.com";
        Integer userId = 1;
        Feedback feedback = Feedback.builder().rate(feedbackRequestRate).
                ticket(Ticket.builder().id(feedbackRequestTicketId).build()).text(feedbackRequestText).build();

        // User from DB by security context
        User creator = User.builder().id(userId).email(contextUserName).build();

        // Ticket
        Ticket ticket = Ticket.builder().id(feedbackRequestTicketId).state(TicketState.DONE).owner(User.builder().
                id(userId).email(contextUserName).build()).assignee(User.builder().build()).build();

        feedback.setTicket(ticket);
        feedback.setUser(creator);

        // FeedbackResponse
        FeedbackResponse expectedFeedbackResponse = FeedbackResponse.builder().
                rate(feedbackRequestRate).text(feedbackRequestText).build();

        Mockito.when(userService.findByEmailService(contextUserName)).thenReturn(creator);
        Mockito.when(ticketService.findByIdService(feedbackRequest.ticketId())).thenReturn(Optional.of(ticket));
        Mockito.when(feedbackRepository.save(Mockito.any(Feedback.class))).thenReturn(feedback);
        Mockito.doNothing().when(emailService).notifyFeedbackProvide(Mockito.any(User.class), Mockito.eq(feedbackRequestTicketId));
        FeedbackResponse actualFeedbackResponse = feedbackService.save(feedbackRequest, contextUserName);

        Mockito.verify(userService, Mockito.times(1)).findByEmailService(contextUserName);
        Mockito.verify(ticketService, Mockito.times(1)).findByIdService(feedbackRequest.ticketId());
        Mockito.verify(feedbackRepository, Mockito.times(1)).save(Mockito.any(Feedback.class));
        Mockito.verify(emailService, Mockito.times(1)).notifyFeedbackProvide(Mockito.any(User.class), Mockito.eq(feedbackRequestTicketId));
        Assertions.assertEquals(expectedFeedbackResponse, actualFeedbackResponse);
    }
}
