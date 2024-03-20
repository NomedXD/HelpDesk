package com.innowise.repo;

import com.innowise.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(Integer id);
    List<Comment> findAll();

    Comment save(Comment comment);
    Comment update(Comment comment);

    void delete(Integer id);

    List<Comment> findAllByTicketId(Integer ticketId);
    List<Comment> findTop5ByTicketId(Integer ticketId);
}
