package com.innowise.service.impl;

import com.innowise.controller.dto.mapper.CommentListMapper;
import com.innowise.controller.dto.mapper.CommentMapper;
import com.innowise.controller.dto.requestDto.CommentRequestDto;
import com.innowise.controller.dto.responseDto.CommentResponseDto;
import com.innowise.domain.Comment;
import com.innowise.repo.CommentRepository;
import com.innowise.repo.TicketRepository;
import com.innowise.repo.UserRepository;
import com.innowise.service.CommentService;
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
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper, CommentListMapper commentListMapper, TicketRepository ticketRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.commentListMapper = commentListMapper;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentResponseDto findById(Integer id) {
        return commentMapper.toCommentResponseDto(
                commentRepository.findById(id).orElseThrow(
                        //todo throw Exception Not Found
                ));
    }

    @Override
    public List<CommentResponseDto> findAll() {
        return commentListMapper.toCommentResponseDtoList(
                commentRepository.findAll()
        );
    }

    @Override
    public List<CommentResponseDto> findAllByTicketId(Integer ticketId) {
        if (ticketRepository.findById(ticketId).isEmpty()) {
            //todo throw Exception Not Found
        }

        return commentListMapper.toCommentResponseDtoList(
                commentRepository.findAllByTicketId(ticketId)
        );
    }

    @Override
    public List<CommentResponseDto> findTop5ByTicketId(Integer ticketId) {
        if (ticketRepository.findById(ticketId).isEmpty()) {
            //todo throw Exception Not Found
        }

        return commentListMapper.toCommentResponseDtoList(
                commentRepository.findTop5ByTicketId(ticketId)
        );
    }

    @Override
    public CommentResponseDto save(CommentRequestDto comment) {
        Comment commentToSave = commentMapper.toComment(comment);

        commentToSave.setUser(userRepository.findById(comment.userId()).orElseThrow(
                //todo throw Exception Not Found
        ));
        if (ticketRepository.findById(comment.userId()).isEmpty()) {
            //todo throw Exception Not Found
        }
        commentToSave.setTicketId(comment.ticketId());
        commentToSave.setDate(LocalDateTime.now());

        return commentMapper.toCommentResponseDto(commentRepository.save(commentToSave));
    }

    @Override
    public CommentResponseDto update(CommentRequestDto comment) {
        return null;
    } //user can't edit comments?

    @Override
    public void delete(Integer id) {
        if (commentRepository.findById(id).isEmpty()) {
            //todo throw Exception Not Found
        }

        commentRepository.delete(id);
    }
}
