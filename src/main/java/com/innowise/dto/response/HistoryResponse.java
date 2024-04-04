package com.innowise.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HistoryResponse(
        LocalDateTime date,
        String userName,
        String action,
        String description) {
}
