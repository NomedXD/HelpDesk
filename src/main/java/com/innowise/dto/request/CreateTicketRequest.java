package com.innowise.dto.request;

import com.innowise.domain.enums.TicketUrgency;
import com.innowise.util.validation.TicketUrgencyValidation;
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
        @Min(1)
        Integer ownerId,

        @NotNull
        @Size(min = 1, max = 100)
        @Pattern(regexp = "([a-z]|[0-9]|~|\\.|\"|\\(|\\)|:|;|\\||<|>|@|\\[|]|!|#|\\$|%|&|'|\\*|\\+|-|/|=|\\?|\\^|_|`|\\{|}| )*")
        String name,

        @Nullable
        @Size(max = 500)
        @Pattern(regexp = "([aA-zZ]|[0-9]|~|\\.|\"|\\(|\\)|:|;|\\||<|>|@|\\[|]|!|#|\\$|%|&|'|\\*|\\+|-|/|=|\\?|\\^|_|`|\\{|}| )*")
        String description,

        @NotNull
        //TODO Проверить работоспособность этой аннотации :)
        @TicketUrgencyValidation(anyOf = {TicketUrgency.CRITICAL, TicketUrgency.HIGH,
                TicketUrgency.MEDIUM, TicketUrgency.LOW})
        TicketUrgency urgency,

        @NotNull
        @Future
        LocalDate desiredResolutionDate,

        /*
         На фронте:
         It is allowed to attach the following file formats: pdf, doc, docx, png, jpeg, jpg.
         If the User selects a file with an invalid extension, the system notifies him:
         “The selected file type is not allowed. Please select a file of one of the following types: pdf, png, doc, docx, jpg, jpeg.”
         The max file size the User can attach is 5 Mb.
         If the size of an attached file exceeds the maximum, the system notifies the User:
         “The size of attached file should not be greater than 5 Mb. Please select another file.”
         */
        @Nullable
        MultipartFile[] files
) {
}
