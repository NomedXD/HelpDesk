package com.innowise.service;

import com.innowise.controller.dto.request.FeedbackRequestDto;
import com.innowise.controller.dto.response.FeedbackResponseDto;

import java.util.List;

public interface FeedbackService {
    FeedbackResponseDto findById(Integer id);
    List<FeedbackResponseDto> findAll();
    List<FeedbackResponseDto> findAllByTicketId(Integer ticketId);

    FeedbackResponseDto save(FeedbackRequestDto feedback);

    void delete(Integer id);
}
