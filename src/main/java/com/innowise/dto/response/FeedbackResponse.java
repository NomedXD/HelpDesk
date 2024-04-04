package com.innowise.dto.response;

import lombok.Builder;

@Builder
public record FeedbackResponse(
        Byte rate,
        String text) {
}
