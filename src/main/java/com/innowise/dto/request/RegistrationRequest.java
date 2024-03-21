package com.innowise.dto.request;

public record RegistrationRequest(
        String email,
        String password,
        String firstName,
        String lastName) {
}
