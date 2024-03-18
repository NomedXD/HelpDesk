package com.innowise.repo;

import com.innowise.domain.Comment;

import java.util.List;

public interface CommentRepository {
    Comment findById(Long id);
    List<Comment> findAll();

    Comment save(Comment comment);
    Comment update(Comment comment);

    void delete(Long id);
}
