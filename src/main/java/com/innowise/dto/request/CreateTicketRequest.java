package com.innowise.dto.request;

import com.innowise.domain.enums.TicketUrgency;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Builder
public record CreateTicketRequest(
        Integer categoryId,
        Integer ownerId,
        String name,
        String description,
        TicketUrgency urgency,
        LocalDate desiredResolutionDate,
        MultipartFile[] files,
        String comment) {
}
