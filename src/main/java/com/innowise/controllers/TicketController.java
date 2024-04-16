package com.innowise.controllers;

import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.request.UpdateTicketRequest;
import com.innowise.dto.request.UpdateTicketStatusRequest;
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
    public ResponseEntity<TicketResponse> createTicket(@ModelAttribute CreateTicketRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.save(request, userDetails.getUsername()));
    }

    // TODO on «edit» button click – change current ticket draft
    //  on «save as Draft» button click – leave current ticket untouched, create new draft ticket
    //  *to ycovich*
    @PutMapping
    public ResponseEntity<TicketResponse> updateTicket(@ModelAttribute UpdateTicketRequest request,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ticketService.update(request, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ticketService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ticketService.findById(id));
    }

    @GetMapping("/personal")
    public ResponseEntity<List<TicketResponse>> getPersonalTickets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ticketService.findAllByAssigneeEmail(userDetails.getUsername()));
    }

    @PostMapping("/status")
    public ResponseEntity<TicketResponse> updateTicketStatus(@RequestBody UpdateTicketStatusRequest updateTicketStatusRequest,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ticketService.updateStatus(updateTicketStatusRequest, userDetails.getUsername()));
    }
}
