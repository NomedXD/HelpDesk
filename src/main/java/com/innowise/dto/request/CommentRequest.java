package com.innowise.dto.request;

import jakarta.validation.constraints.*;

public record CommentRequest(
        @NotNull
        @Min(1)
        Integer userId,

        @NotBlank
        @Size(min = 1, max = 500)
        @Pattern(regexp = "([aA-zZ]|[0-9]|~|\\.|\"|\\(|\\)|:|;|\\||<|>|@|\\[|]|!|#|\\$|%|&|'|\\*|\\+|-|/|=|\\?|\\^|_|`|\\{|}| )*")
        String text,

        @NotNull
        @Min(1)
        Integer ticketId) {
}
