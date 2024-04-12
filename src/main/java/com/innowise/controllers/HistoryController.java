package com.innowise.controllers;

import com.innowise.dto.response.HistoryResponse;
import com.innowise.services.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/api/tickets/{id}/histories")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesByTicketId(@PathVariable Integer id) {
        return ResponseEntity.ok(historyService.findAllByTicketId(id));
    }
}
