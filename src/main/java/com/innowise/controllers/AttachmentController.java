package com.innowise.controllers;

import com.innowise.dto.response.FileChunkResponse;
import com.innowise.dto.response.FileInfoResponse;
import com.innowise.services.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @GetMapping(value = "/{id}/chunk", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> getAttachmentChunk(
            @PathVariable("id") Integer id,
            @RequestParam("start") Integer start,
            @RequestParam("length") Integer length) throws IOException {
        FileChunkResponse response = attachmentService.getFileById(id, start, length);

        return ResponseEntity.ok()
                .contentLength(response.chunk().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(response.resource().getInputStream()));
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<FileInfoResponse> getAttachmentInfo(@PathVariable("id") Integer id) {
        FileInfoResponse info = attachmentService.getFileInfoById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }
}
