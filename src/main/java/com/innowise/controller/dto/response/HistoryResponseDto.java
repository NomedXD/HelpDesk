package com.innowise.controller.dto.response;

import java.time.LocalDateTime;

public record HistoryResponseDto(
        LocalDateTime date,
        String userName,
        String action,
        String description) {
}
