package com.innowise.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FeedbackRequest(
        @NotNull
        @Min(1)
        @Max(5)
        Byte rate,

        @Nullable
        @Size(max = 500)
        @Pattern(regexp = "([aA-zZ]|[0-9]|~|\\.|\"|\\(|\\)|:|;|\\||<|>|@|\\[|]|!|#|\\$|%|&|'|\\*|\\+|-|/|=|\\?|\\^|_|`|\\{|}| )*")
        String text,

        @NotNull
        @Min(1)
        Integer ticketId) {
}
