package com.innowise.services;

import com.innowise.domain.Attachment;
import com.innowise.dto.response.FileChunkResponse;
import com.innowise.dto.response.FileInfoResponse;

import java.util.List;

public interface AttachmentService {
    Attachment getById(Integer id);
    FileChunkResponse getFileById(Integer id, Integer start, Integer length);
    FileInfoResponse getFileInfoById(Integer id);
    void replaceAttachmentsByTicketId(Integer ticketId, List<Attachment> attachmentList);
}
