package com.innowise.services.impl;

import com.innowise.domain.Ticket;
import com.innowise.exceptions.EntityTypeMessages;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.util.mappers.CommentListMapper;
import com.innowise.util.mappers.CommentMapper;
import com.innowise.dto.request.CommentRequest;
import com.innowise.dto.response.CommentResponse;
import com.innowise.domain.Comment;
import com.innowise.domain.User;
import com.innowise.repositories.CommentRepository;
import com.innowise.services.CommentService;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Validated
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommentListMapper commentListMapper;
    private final UserService userService;
    private TicketService ticketService;

    @Override
    @Validated
    public CommentResponse save(CommentRequest commentRequest) {
        Comment comment = commentMapper.toComment(commentRequest);
        User creator = userService.getUserFromPrincipal();
        comment.setUser(creator);
        comment.setTicket(ticketService.findByIdService(creator.getId())
                .orElseThrow(() ->
                        new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, commentRequest.ticketId())));
        comment.setDate(LocalDateTime.now());

        return commentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponse> findAll() {
        return commentListMapper.toCommentResponseDtoList(
                commentRepository.findAll()
        );
    }

    // User can't edit comments
    @Override
    @Validated
    public CommentResponse update(CommentRequest commentRequest) {
        return commentMapper.toCommentResponseDto(commentRepository.update(commentMapper.toComment(commentRequest)));
    }

    @Override
    public void delete(Integer id) {
        if (commentRepository.existsById(id)) {
            commentRepository.delete(id);
        } else {
            throw new NoSuchEntityIdException(EntityTypeMessages.COMMENT_MESSAGE, id);
        }
    }

    @Override
    public CommentResponse findById(Integer id) {
        return commentMapper.toCommentResponseDto(
                commentRepository.findById(id).orElseThrow(() ->
                        new NoSuchEntityIdException(EntityTypeMessages.COMMENT_MESSAGE, id)));
    }

    @Override
    public List<CommentResponse> findAllByTicketId(Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            return commentListMapper.toCommentResponseDtoList(commentRepository.findAllByTicketId(ticketId));
        } else {
            throw new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, ticketId);
        }
    }

    @Override
    public List<CommentResponse> findPaginatedByTicketId(Integer page, Integer pageSize, Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            return commentListMapper.toCommentResponseDtoList(commentRepository.findPaginatedByTicketId(page, pageSize, ticketId));
        } else {
            throw new NoSuchEntityIdException(EntityTypeMessages.TICKET_MESSAGE, ticketId);
        }
    }

    @Override
    public Comment saveByTicket(User user, String commentText, Ticket ticket) {
        Comment comment = Comment.builder().date(LocalDateTime.now()).user(user).text(commentText).ticket(ticket).build();
        return commentRepository.save(comment);
    }

    @Autowired
    @Lazy
    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }
}
