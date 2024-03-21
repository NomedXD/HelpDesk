package com.innowise.repositories;

import com.innowise.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> findAll();

    Comment update(Comment comment);

    void delete(Integer id);

    Optional<Comment> findById(Integer id);

    boolean existsById(Integer id);

    List<Comment> findAllByTicketId(Integer ticketId);

    List<Comment> findPaginatedByTicketId(Integer page, Integer pageSize, Integer ticketId);
}
