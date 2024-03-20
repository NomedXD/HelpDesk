package com.innowise.repo.impl;

import com.innowise.domain.Feedback;
import com.innowise.repo.FeedbackRepository;
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
    public Optional<Feedback> findById(Integer id) {
        return Optional.ofNullable(session.find(Feedback.class, id));
    }

    @Override
    public List<Feedback> findAll() {
        return session.createQuery("From Feedback", Feedback.class).list();
    }

    @Override
    public Feedback save(Feedback feedback) {
        return session.merge(feedback);
    }

    @Override
    public Feedback update(Feedback feedback) {
        return session.merge(feedback);
    }

    @Override
    public void delete(Integer id) {
        Optional<Feedback> feedbackToDelete = Optional.ofNullable(session.find(Feedback.class, id));
        if (feedbackToDelete.isEmpty()) {
            //todo throw exception
        }

        session.remove(feedbackToDelete);
    }
}
