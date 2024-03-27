package com.innowise.controllers;

import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> uploadEntityWithFiles(@ModelAttribute CreateTicketRequest request) {
        try {
            TicketResponse response = ticketService.save(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload entity");
        }
    }


    // TODO COMING IN HOT TRY RETRIEVE MODEL (IT WILL BIND EVERYTHING)
    // TODO think about form-data and DTO construction problem

    // TODO zip download
    // TODO single file download
    // TODO to ycovich

    @GetMapping("/all")
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.findAll());
    }
    @GetMapping("/personal")
    public ResponseEntity<List<TicketResponse>> getPersonalTickets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.findAllByAssigneeEmail(userDetails.getUsername()));
    }
}
