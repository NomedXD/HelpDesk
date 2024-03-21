package com.innowise.dto.request;

public record FeedbackRequest(
        Integer userId,
        Byte rate,
        String text,
        Integer ticketId) {
}
