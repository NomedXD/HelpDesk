package com.innowise.controller.dto.response;

public record AttachmentResponseDto(
        String name,
        byte[] blob
) {
}
