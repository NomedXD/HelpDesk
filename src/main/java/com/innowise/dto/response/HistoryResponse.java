package com.innowise.dto.response;

import java.time.LocalDateTime;

public record HistoryResponse(
        LocalDateTime date,
        String userEmail,
        String action,
        String description) {
}
