package com.innowise.controllers;

import com.innowise.dto.request.CommentRequest;
import com.innowise.dto.response.CommentResponse;
import com.innowise.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping()
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/comments")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.save(commentRequest));
    }

    @GetMapping("/api/tickets/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByTicket(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.findAllByTicketId(id));
    }
}