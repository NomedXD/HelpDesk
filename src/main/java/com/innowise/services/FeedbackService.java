package com.innowise.services;

import com.innowise.dto.request.FeedbackRequest;
import com.innowise.dto.response.FeedbackResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse save(@Valid FeedbackRequest feedbackRequest);

    List<FeedbackResponse> findAll();

    void delete(Integer id);

    FeedbackResponse findById(Integer id);

    FeedbackResponse findByTicketId(Integer ticketId);
}
