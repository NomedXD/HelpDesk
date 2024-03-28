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

    // TODO add flag to CreateTicketRequest to determine if draft or not
    @PostMapping
    public ResponseEntity<?> createTicket(@ModelAttribute CreateTicketRequest request) {
        try {
            TicketResponse response = ticketService.save(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload entity");
        }
    }

    // TODO retrieve a Page maybe, not a List
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok()
                .body(ticketService.findAll());
    }

    // TODO security annotation for role MANAGER
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable("id") Integer id) {
        return ResponseEntity.ok()
                .body(ticketService.findById(id));
    }


    @GetMapping("/personal")
    public ResponseEntity<List<TicketResponse>> getPersonalTickets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok()
                .body(ticketService.findAllByAssigneeEmail(userDetails.getUsername()));
    }
}
