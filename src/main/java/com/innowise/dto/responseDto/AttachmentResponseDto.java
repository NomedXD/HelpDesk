package com.innowise.dto.responseDto;

public record AttachmentResponseDto(
        String name,
        byte[] blob
) {
}
