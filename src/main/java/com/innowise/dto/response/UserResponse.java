package com.innowise.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Integer id,
        String email,
        String firstName,
        String lastName
) {
}
