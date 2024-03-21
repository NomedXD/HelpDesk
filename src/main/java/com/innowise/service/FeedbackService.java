package com.innowise.service;

import com.innowise.controller.dto.requestDto.FeedbackRequestDto;
import com.innowise.controller.dto.responseDto.FeedbackResponseDto;

import java.util.List;

public interface FeedbackService {
    FeedbackResponseDto findById(Integer id);
    List<FeedbackResponseDto> findAll();
    List<FeedbackResponseDto> findAllByTicketId(Integer ticketId);

    FeedbackResponseDto save(FeedbackRequestDto feedback);

    void delete(Integer id);
}
