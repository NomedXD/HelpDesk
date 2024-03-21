package com.innowise.services;

import com.innowise.dto.requestDto.FeedbackRequestDto;
import com.innowise.dto.responseDto.FeedbackResponseDto;

import java.util.List;

public interface FeedbackService {
    FeedbackResponseDto save(FeedbackRequestDto feedbackRequestDto);

    List<FeedbackResponseDto> findAll();

    void delete(Integer id);

    FeedbackResponseDto findById(Integer id);

    List<FeedbackResponseDto> findAllByTicketId(Integer ticketId);
}
