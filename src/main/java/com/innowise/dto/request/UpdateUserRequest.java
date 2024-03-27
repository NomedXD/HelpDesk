package com.innowise.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])$")
        @Size(min = 1, max = 30)
        String firstName,

        @NotBlank
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])$")
        @Size(min = 1, max = 30)
        String lastName
) {
}
