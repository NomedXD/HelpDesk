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
public record UpdateTicketRequest(
        @NotNull
        @Min(1)
        Integer id,
        @NotNull
        @Min(1)
        Integer categoryId,
        @NotNull
        @Size(min = 1, max = 100)
        @Pattern(regexp = "[.]+")
        String name,
        @Nullable
        @Size(max = 500)
        @Pattern(regexp = "[.]+")
        String description,
        @NotNull
        //TODO Проверить работоспособность этой аннотации :)
        @TicketUrgencyValidation(anyOf = {TicketUrgency.CRITICAL, TicketUrgency.HIGH,
                TicketUrgency.MEDIUM, TicketUrgency.LOW})
        TicketUrgency urgency,
        @NotNull
        @Future
        LocalDate desiredResolutionDate,
        @Nullable
        MultipartFile[] files,
        @Nullable
        @Size(max = 500)
        @Pattern(regexp = "[.]+")
        String comment) {
}
