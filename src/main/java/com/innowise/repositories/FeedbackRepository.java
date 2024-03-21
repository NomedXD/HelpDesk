package com.innowise.repositories;

import com.innowise.domain.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository {
    Feedback save(Feedback feedback);

    List<Feedback> findAll();

    Feedback update(Feedback feedback);

    void delete(Integer id);

    boolean existsById(Integer id);

    Optional<Feedback> findById(Integer id);

    List<Feedback> findAllByTicketId(Integer ticketId);
}
