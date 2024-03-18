package com.innowise.controller.dto.responseDto;

import java.time.LocalDateTime;

public record CommentResponseDto(
        LocalDateTime date,
        String userName,
        String text) {
}
