package com.innowise.services.impl;

import com.innowise.util.mappers.CommentListMapper;
import com.innowise.util.mappers.CommentMapper;
import com.innowise.dto.request.CommentRequest;
import com.innowise.dto.response.CommentResponse;
import com.innowise.domain.Comment;
import com.innowise.domain.User;
import com.innowise.exceptions.NoSuchCommentException;
import com.innowise.exceptions.NoSuchTicketException;
import com.innowise.repositories.CommentRepository;
import com.innowise.services.CommentService;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommentListMapper commentListMapper;
    private final TicketService ticketService;
    private final UserService userService;

    @Override
    public CommentResponse save(CommentRequest commentRequest) {
        Comment comment = commentMapper.toComment(commentRequest);

        comment.setUser(userService.findById(commentRequest.userId()));
        comment.setTicketId(ticketService.findByIdService(commentRequest.userId()).orElseThrow(() -> new NoSuchTicketException(commentRequest.ticketId())).getId());
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
    public CommentResponse update(CommentRequest commentRequest) {
        return commentMapper.toCommentResponseDto(commentRepository.update(commentMapper.toComment(commentRequest)));
    }

    @Override
    public void delete(Integer id) {
        if (commentRepository.existsById(id)) {
            commentRepository.delete(id);
        } else {
            throw new NoSuchCommentException(id);
        }
    }

    @Override
    public CommentResponse findById(Integer id) {
        return commentMapper.toCommentResponseDto(
                commentRepository.findById(id).orElseThrow(() -> new NoSuchCommentException(id)));
    }

    @Override
    public List<CommentResponse> findAllByTicketId(Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            return commentListMapper.toCommentResponseDtoList(commentRepository.findAllByTicketId(ticketId));
        } else {
            throw new NoSuchTicketException(ticketId);
        }
    }

    @Override
    public List<CommentResponse> findPaginatedByTicketId(Integer page, Integer pageSize, Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            return commentListMapper.toCommentResponseDtoList(commentRepository.findPaginatedByTicketId(page, pageSize, ticketId));
        } else {
            throw new NoSuchTicketException(ticketId);
        }
    }

    @Override
    public Comment saveByTicket(User user, String commentText, Integer ticketId) {
        Comment comment = Comment.builder().date(LocalDateTime.now()).user(user).text(commentText).ticketId(ticketId).build();
        return commentRepository.save(comment);
    }
}
