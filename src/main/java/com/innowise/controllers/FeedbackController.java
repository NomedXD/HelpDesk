package com.innowise.controllers;

import com.innowise.dto.request.FeedbackRequest;
import com.innowise.dto.response.FeedbackResponse;
import com.innowise.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/{id}/feedback")
    public ResponseEntity<FeedbackResponse> createFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(feedbackService.save(feedbackRequest));
    }

    @GetMapping("/{id}/feedback")
    public ResponseEntity<FeedbackResponse> getFeedback(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(feedbackService.findByTicketId(id));
    }
}