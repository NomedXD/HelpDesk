package com.innowise.dto.request;

public record LoginRequest(
        String email,
        String password) {
}
