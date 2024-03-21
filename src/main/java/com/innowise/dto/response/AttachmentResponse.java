package com.innowise.dto.response;

public record AttachmentResponse(
        String name,
        byte[] blob
) {
}
