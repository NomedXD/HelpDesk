package com.innowise.dto.request;

public record CommentRequest(
        // Id filed is not needed
        Integer userId,
        String text,
        // date field must be established on server
        Integer ticketId) {
}
