package com.innowise.services.impl;

import com.innowise.domain.Category;
import com.innowise.domain.Comment;
import com.innowise.domain.Ticket;
import com.innowise.domain.User;
import com.innowise.dto.request.CommentRequest;
import com.innowise.dto.response.CommentResponse;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.repositories.CommentRepository;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import com.innowise.util.mappers.CommentListMapper;
import com.innowise.util.mappers.CommentListMapperImpl;
import com.innowise.util.mappers.CommentMapper;
import com.innowise.util.mappers.CommentMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CommentRepository commentRepository;
    @Spy
    private final CommentListMapper commentListMapper = new CommentListMapperImpl(new CommentMapperImpl());
    @Spy
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    @Mock
    private UserService userService;
    @Mock
    private TicketService ticketService;

    @Test
    public void save_withValidTicketId_savesComment() {
        // CommentRequest
        String commentRequestText = "commentRequestText";
        Integer commentRequestTicketId = 1;
        CommentRequest commentRequest = CommentRequest.builder().text(commentRequestText).
                ticketId(commentRequestTicketId).build();

        // Comment after mapping
        String contextUserName = "user@example.com";
        String userFirstName = "userFirstName";
        LocalDateTime commentDate = LocalDateTime.now();
        Comment comment = Comment.builder().text(commentRequestText).
                ticket(Ticket.builder().id(commentRequestTicketId).build()).
                user(User.builder().firstName(userFirstName).build()).date(commentDate).build();

        // User from DB by security context
        User creator = User.builder().email(contextUserName).build();

        // Ticket
        Ticket ticket = Ticket.builder().id(commentRequestTicketId).build();
        CommentResponse expectedCommentResponse = CommentResponse.builder().
                text(commentRequestText).userName(userFirstName).date(commentDate).build();

        Mockito.when(userService.findByEmailService(contextUserName)).thenReturn(creator);
        Mockito.when(ticketService.findByIdService(commentRequest.ticketId())).thenReturn(Optional.of(ticket));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        CommentResponse actualCommentResponse = commentService.save(commentRequest, contextUserName);

        Mockito.verify(userService, Mockito.times(1)).findByEmailService(contextUserName);
        Mockito.verify(ticketService, Mockito.times(1)).findByIdService(commentRequest.ticketId());
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any(Comment.class));
        Assertions.assertEquals(expectedCommentResponse, actualCommentResponse);
    }

    @Test
    public void save_withInvalidTicketId_throwsNoSuchEntityIdException() {
        // CommentRequest
        String commentRequestText = "commentRequestText";
        Integer commentRequestTicketId = 999;
        CommentRequest commentRequest = CommentRequest.builder().text(commentRequestText).
                ticketId(commentRequestTicketId).build();

        // Comment after mapping
        String contextUserName = "user@example.com";

        // User from DB by security context
        User creator = User.builder().email(contextUserName).build();

        Mockito.when(userService.findByEmailService(contextUserName)).thenReturn(creator);
        Mockito.when(ticketService.findByIdService(commentRequest.ticketId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchEntityIdException.class, () -> commentService.save(commentRequest, contextUserName));
        Mockito.verify(userService, Mockito.times(1)).findByEmailService(contextUserName);
        Mockito.verify(ticketService, Mockito.times(1)).findByIdService(commentRequest.ticketId());
        Mockito.verify(commentRepository, Mockito.times(0)).save(Mockito.any(Comment.class));
    }

    @Test
    public void findAll_withValid_returnsCommentList() {
        // Comments
        LocalDateTime commentDate1 = LocalDateTime.now();
        LocalDateTime commentDate2 = LocalDateTime.now();
        String commentFirstName = "commentUserName";
        String commentText1 = "commentText1";
        String commentText2 = "commentText1";
        Comment comment1 = Comment.builder().date(commentDate1).
                user(User.builder().firstName(commentFirstName).build()).text(commentText1).build();
        Comment comment2 = Comment.builder().date(commentDate2).
                user(User.builder().firstName(commentFirstName).build()).text(commentText2).build();
        List<Comment> commentList = Arrays.asList(comment1, comment2);
        Mockito.when(commentRepository.findAll()).thenReturn(commentList);

        List<CommentResponse> expectedCommentResponseList = List.of(
                CommentResponse.builder().date(commentDate1).userName(commentFirstName).text(commentText1).build(),
                CommentResponse.builder().date(commentDate2).userName(commentFirstName).text(commentText2).build());

        List<CommentResponse> actualCommentResponseList = commentService.findAll();

        Mockito.verify(commentRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(commentList.size(), actualCommentResponseList.size());
        Assertions.assertEquals(expectedCommentResponseList, actualCommentResponseList);
    }
}
