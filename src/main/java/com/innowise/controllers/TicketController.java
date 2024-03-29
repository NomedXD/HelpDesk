package com.innowise.controllers;

import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.request.UpdateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.services.TicketService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<TicketResponse> createTicket(@ModelAttribute CreateTicketRequest request) {
        return ResponseEntity.ok(ticketService.save(request));
    }

    @PutMapping
    public ResponseEntity<TicketResponse> updateTicket(@RequestBody UpdateTicketRequest request) {
        return ResponseEntity.ok(ticketService.update(request));
    }

    // TODO retrieve a Page maybe, not a List *NOT URGENT*
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok()
                .body(ticketService.findAll());
    }

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
