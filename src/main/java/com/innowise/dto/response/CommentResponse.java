package com.innowise.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        LocalDateTime date,
        String userName,
        String text) {
}
