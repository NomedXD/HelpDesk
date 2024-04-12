package com.innowise.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        LocalDateTime date,
        String userEmail,
        String text) {
}
