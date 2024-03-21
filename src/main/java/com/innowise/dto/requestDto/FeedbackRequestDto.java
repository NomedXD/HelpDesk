package com.innowise.dto.requestDto;

public record FeedbackRequestDto(
        Integer userId,
        Byte rate,
        String text,
        Integer ticketId) {
}
