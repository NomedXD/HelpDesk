package com.innowise.controller.dto.response;

import java.time.LocalDateTime;

public record CommentResponseDto(
        LocalDateTime date,
        String userName,
        String text) {
}
