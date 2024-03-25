package com.innowise.controllers;

import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> uploadEntityWithFiles(@RequestParam("name") String name,
                                                   @RequestParam("files") MultipartFile[] files) {
        CreateTicketRequest request = CreateTicketRequest.builder()
                .name(name)
                .files(files)
                .build();
        try {
            TicketResponse response = ticketService.save(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload entity");
        }
    }

    // TODO zip download
    // TODO single file download
    // TODO to ycovich

}
