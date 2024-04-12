package com.innowise.dto.request;

import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.TicketUrgency;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Builder
public record CreateTicketRequest(
        @NotNull
        @Min(1)
        Integer categoryId,

        @NotNull
        @Size(min = 1, max = 100)
        @Pattern(regexp = "([a-z]|[0-9]|~|\\.|\"|\\(|\\)|:|;|\\||<|>|@|\\[|]|!|#|\\$|%|&|'|\\*|\\+|-|/|=|\\?|\\^|_|`|\\{|}| )*")
        String name,

        @Nullable
        @Size(max = 500)
        @Pattern(regexp = "([aA-zZ]|[0-9]|~|\\.|\"|\\(|\\)|:|;|\\||<|>|@|\\[|]|!|#|\\$|%|&|'|\\*|\\+|-|/|=|\\?|\\^|_|`|\\{|}| )*")
        String description,

        @NotNull
        TicketUrgency urgency,

        @NotNull
        @Future
        LocalDate desiredResolutionDate,

        @Nullable
        MultipartFile[] files,

        @NotNull
        TicketState state
) {
}
