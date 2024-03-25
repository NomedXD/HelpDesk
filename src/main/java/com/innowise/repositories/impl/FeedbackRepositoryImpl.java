package com.innowise.repositories.impl;

import com.innowise.domain.Feedback;
import com.innowise.repositories.FeedbackRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepository {
    @PersistenceContext
    private Session session;

    @Override
    public Feedback save(Feedback feedback) {
        return session.merge(feedback);
    }

    @Override
    public List<Feedback> findAll() {
        return session.createQuery("FROM Feedback", Feedback.class).list();
    }

    @Override
    public Feedback update(Feedback feedback) {
        return session.merge(feedback);
    }

    @Override
    public void delete(Integer id) {
        session.createMutationQuery("DELETE FROM Feedback WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(Integer id) {
        return session.createNativeQuery("SELECT COUNT(*) FROM feedbacks WHERE id = :id", Integer.class)
                .setParameter("id", id)
                .getSingleResult() == 1;
    }

    @Override
    public Optional<Feedback> findById(Integer id) {
        return Optional.ofNullable(session.find(Feedback.class, id));
    }

    @Override
    public List<Feedback> findAllByTicketId(Integer ticketId) {
        return session.createQuery("FROM Feedback WHERE ticket.id = :id ORDER BY date DESC", Feedback.class)
                .setParameter("id", ticketId)
                .list();
    }
}
