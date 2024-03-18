package com.innowise.controller.dto.responseDto;

public record AttachmentResponseDto(
        String name,
        byte[] blob
) {
}
