package com.innowise.controller;

import com.innowise.controller.dto.request.TicketRequest;
import com.innowise.domain.Ticket;
import com.innowise.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadEntityWithFiles(@RequestParam("name") String name,
                                                   @RequestParam("files") MultipartFile[] files) {
        TicketRequest request = TicketRequest.builder()
                .name(name)
                .files(files)
                .build();
        try {
            Ticket ticket = ticketService.save(request);
            return ResponseEntity.ok(ticket.getName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload entity");
        }
    }
}
