package com.innowise.repositories.impl;

import com.innowise.domain.Comment;
import com.innowise.repositories.CommentRepository;
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
    public Comment save(Comment comment) {
        return session.merge(comment);
    }

    @Override
    public List<Comment> findAll() {
        return session.createQuery("FROM Comment", Comment.class).list();
    }

    @Override
    public Comment update(Comment comment) {
        return session.merge(comment);
    }

    @Override
    public Optional<Comment> findById(Integer id) {
        return Optional.ofNullable(session.find(Comment.class, id));
    }

    @Override
    public void delete(Integer id) {
        session.createQuery("DELETE Comment WHERE id = :id", Comment.class)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Comment> findAllByTicketId(Integer ticketId) {
        return session.createQuery("FROM Comment WHERE ticketId = :id ORDER BY date DESC ", Comment.class)
                .setParameter("id", ticketId)
                .list();
    }

    @Override
    public List<Comment> findPaginatedByTicketId(Integer page, Integer pageSize, Integer ticketId) {
        return session.createQuery("FROM Comment WHERE ticketId = :id ORDER BY date DESC ", Comment.class)
                .setParameter("id", ticketId)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }
}
