package com.innowise.security.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Token(
        UUID id,
        Integer userId,
        String subject,
        List<String> authorities,
        Instant createdAt,
        Instant expiresAt
) {
}
