package com.innowise.controller.dto.request;

public record RegistrationRequestDto(
        String email,
        String password,
        String firstName,
        String lastName) {
}
