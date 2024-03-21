package com.innowise.controller.dto.response;

public record FeedbackResponseDto(
        Byte rate,
        String text) {
}
