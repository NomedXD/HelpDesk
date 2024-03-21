package com.innowise.dto.requestDto;

public record CommentRequestDto(
        // Id filed is not needed
        Integer userId,
        String text,
        // date field must be established on server
        Integer ticketId) {
}
