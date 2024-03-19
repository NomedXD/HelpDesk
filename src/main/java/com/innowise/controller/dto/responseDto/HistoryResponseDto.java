package com.innowise.controller.dto.responseDto;

import java.time.LocalDateTime;

public record HistoryResponseDto(
        LocalDateTime date,
        String userName,
        String action,
        String description) {
}
