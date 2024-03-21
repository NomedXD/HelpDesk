package com.innowise.dto.requestDto;

public record RegistrationRequestDto(
        String email,
        String password,
        String firstName,
        String lastName) {
}
