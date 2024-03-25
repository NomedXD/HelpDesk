package com.innowise.services;

import com.innowise.domain.Ticket;
import com.innowise.dto.request.CommentRequest;
import com.innowise.dto.response.CommentResponse;
import com.innowise.domain.Comment;
import com.innowise.domain.User;
import jakarta.validation.Valid;

import java.util.List;

public interface CommentService {

    CommentResponse save(@Valid CommentRequest commentRequest);

    List<CommentResponse> findAll();

    CommentResponse update(@Valid CommentRequest commentRequest);

    void delete(Integer id);

    CommentResponse findById(Integer id);

    List<CommentResponse> findAllByTicketId(Integer ticketId);

    List<CommentResponse> findPaginatedByTicketId(Integer page, Integer pageSize, Integer ticketId);

    Comment saveByTicket(User user, String commentText, Ticket ticket);
}
