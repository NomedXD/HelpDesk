package com.innowise.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        // Id filed is not needed
        @NotNull
        @Min(1)
        Integer userId,
        @NotNull
        @Size(min = 1, max = 500)
        @Pattern(regexp = "[.]+")
        String text,
        @NotNull
        @Min(1)
        Integer ticketId) {
}
