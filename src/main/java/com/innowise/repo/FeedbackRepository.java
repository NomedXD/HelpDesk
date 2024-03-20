package com.innowise.repo;

import com.innowise.domain.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository {
    Optional<Feedback> findById(Integer id);
    List<Feedback> findAll();

    Feedback save(Feedback feedback);
    Feedback update(Feedback feedback);

    void delete(Integer id);
}
