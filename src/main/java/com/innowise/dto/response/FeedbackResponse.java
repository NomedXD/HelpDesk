package com.innowise.dto.response;

public record FeedbackResponse(
        Byte rate,
        String text) {
}
