package com.innowise.service;

import com.innowise.controller.dto.request.CommentRequestDto;
import com.innowise.controller.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto findById(Integer id);
    List<CommentResponseDto> findAll();
    List<CommentResponseDto> findAllByTicketId(Integer ticketId);
    List<CommentResponseDto> findTop5ByTicketId(Integer ticketId); //todo think about method name

    CommentResponseDto save(CommentRequestDto comment);
    CommentResponseDto update(CommentRequestDto comment);

    void delete(Integer id);
}
