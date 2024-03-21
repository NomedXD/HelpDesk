package com.innowise.controller.dto.request;

import com.innowise.domain.enums.TicketUrgency;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

// Only for Creation and editing by creator
public record TicketRequest(
        String name,
        String description,
        TicketUrgency urgency,
        Integer categoryId,
        Integer ownerId,
        Integer assigneeId,
        LocalDate desiredResolutionDate,
        MultipartFile[] files) {
}
