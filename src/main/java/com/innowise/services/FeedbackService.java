package com.innowise.services;

import com.innowise.dto.request.FeedbackRequest;
import com.innowise.dto.response.FeedbackResponse;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse save(FeedbackRequest feedbackRequest);

    List<FeedbackResponse> findAll();

    void delete(Integer id);

    FeedbackResponse findById(Integer id);

    List<FeedbackResponse> findAllByTicketId(Integer ticketId);
}
