package com.innowise.repo.impl;

import com.innowise.domain.Comment;
import com.innowise.repo.CommentRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    @PersistenceContext
    private Session session;

    @Override
    public Comment findById(Long id) {
        return session.find(Comment.class, id);
    }

    @Override
    public List<Comment> findAll() {
        return session.createQuery("From Comment", Comment.class).list();
    }

    @Override
    public Comment save(Comment comment) {
        return session.merge(comment);
    }

    @Override
    public Comment update(Comment comment) {
        return session.merge(comment);
    }

    @Override
    public void delete(Long id) {
        Optional<Comment> commentToDelete = Optional.of(session.find(Comment.class, id));
        if (commentToDelete.isEmpty()) {
            //todo throw exception
        }

        session.remove(commentToDelete);
    }
}
