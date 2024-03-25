package com.innowise.dto.response;

public record UserResponse(
        Integer id,
        String email,
        String firstName,
        String lastName
) {
}
