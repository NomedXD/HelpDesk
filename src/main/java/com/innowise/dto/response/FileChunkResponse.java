package com.innowise.dto.response;

import org.springframework.core.io.ByteArrayResource;

public record FileChunkResponse(
        ByteArrayResource resource,
        byte[] chunk
){
}
