package com.innowise.repo;

import com.innowise.domain.Feedback;

import java.util.List;

public interface FeedbackRepository {
    Feedback findById(Long id);
    List<Feedback> findAll();

    Feedback save(Feedback feedback);
    Feedback update(Feedback feedback);

    void delete(Long id);
}
