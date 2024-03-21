package com.innowise.services;

import com.innowise.dto.requestDto.CommentRequestDto;
import com.innowise.dto.responseDto.CommentResponseDto;
import com.innowise.domain.Comment;
import com.innowise.domain.User;

import java.util.List;

public interface CommentService {

    CommentResponseDto save(CommentRequestDto commentRequestDto);

    List<CommentResponseDto> findAll();

    CommentResponseDto update(CommentRequestDto commentRequestDto);

    void delete(Integer id);

    CommentResponseDto findById(Integer id);

    List<CommentResponseDto> findAllByTicketId(Integer ticketId);

    List<CommentResponseDto> findPaginatedByTicketId(Integer page, Integer pageSize, Integer ticketId);

    Comment saveByTicket(User user, String commentText, Integer ticketId);
}
