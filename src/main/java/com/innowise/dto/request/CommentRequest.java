package com.innowise.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record CommentRequest(
        @NotBlank
        @Size(min = 1, max = 500)
        @Pattern(regexp = "([aA-zZ]|[0-9]|~|\\.|\"|\\(|\\)|:|;|\\||<|>|@|\\[|]|!|#|\\$|%|&|'|\\*|\\+|-|/|=|\\?|\\^|_|`|\\{|}| )*")
        String text,

        @NotNull
        @Min(1)
        Integer ticketId) {
}
