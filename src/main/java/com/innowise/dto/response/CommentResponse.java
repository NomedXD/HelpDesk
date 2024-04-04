package com.innowise.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        LocalDateTime date,
        String userName,
        String text) {
}
