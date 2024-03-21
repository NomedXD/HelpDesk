package com.innowise.services.impl;

import com.innowise.dto.mappers.CommentListMapper;
import com.innowise.dto.mappers.CommentMapper;
import com.innowise.dto.requestDto.CommentRequestDto;
import com.innowise.dto.responseDto.CommentResponseDto;
import com.innowise.domain.Comment;
import com.innowise.domain.User;
import com.innowise.exceptions.NoSuchCommentException;
import com.innowise.exceptions.NoSuchTicketException;
import com.innowise.exceptions.NoSuchUserIdException;
import com.innowise.repositories.CommentRepository;
import com.innowise.services.CommentService;
import com.innowise.services.TicketService;
import com.innowise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommentListMapper commentListMapper;
    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper, CommentListMapper commentListMapper, TicketService ticketService, UserService userService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.commentListMapper = commentListMapper;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @Override
    public CommentResponseDto save(CommentRequestDto commentRequestDto) {
        Comment comment = commentMapper.toComment(commentRequestDto);

        comment.setUser(userService.findByIdService(commentRequestDto.userId()).orElseThrow(() -> new NoSuchUserIdException(commentRequestDto.userId())));
        comment.setTicketId(ticketService.findByIdService(commentRequestDto.userId()).orElseThrow(() -> new NoSuchTicketException(commentRequestDto.ticketId())).getId());
        comment.setDate(LocalDateTime.now());

        return commentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponseDto> findAll() {
        return commentListMapper.toCommentResponseDtoList(
                commentRepository.findAll()
        );
    }

    // User can't edit comments
    @Override
    public CommentResponseDto update(CommentRequestDto commentRequestDto) {
        return commentMapper.toCommentResponseDto(commentRepository.update(commentMapper.toComment(commentRequestDto)));
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
    public CommentResponseDto findById(Integer id) {
        return commentMapper.toCommentResponseDto(
                commentRepository.findById(id).orElseThrow(() -> new NoSuchCommentException(id)));
    }

    @Override
    public List<CommentResponseDto> findAllByTicketId(Integer ticketId) {
        if (ticketService.existsByIdService(ticketId)) {
            return commentListMapper.toCommentResponseDtoList(commentRepository.findAllByTicketId(ticketId));
        } else {
            throw new NoSuchTicketException(ticketId);
        }
    }

    @Override
    public List<CommentResponseDto> findPaginatedByTicketId(Integer page, Integer pageSize, Integer ticketId) {
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
