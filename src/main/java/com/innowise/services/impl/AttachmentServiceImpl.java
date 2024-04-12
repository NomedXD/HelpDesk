package com.innowise.services.impl;

import com.innowise.domain.Attachment;
import com.innowise.dto.response.FileChunkResponse;
import com.innowise.dto.response.FileInfoResponse;
import com.innowise.exceptions.EntityTypeMessages;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.repositories.AttachmentRepository;
import com.innowise.services.AttachmentService;
import com.innowise.util.MimeDetector;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final MimeDetector mimeDetector;

    @Override
    public Attachment getById(Integer id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityIdException(EntityTypeMessages.ATTACHMENT_MESSAGE, id));
    }

    @Override
    public FileChunkResponse getFileById(Integer id, Integer start, Integer length) {
        Attachment attachment = getById(id);

        if (start >= attachment.getBlob().length) {
            throw new IllegalArgumentException();
        }
        int end = Math.min(start + length, attachment.getBlob().length);
        byte[] chunk = Arrays.copyOfRange(attachment.getBlob(), start, end);
        ByteArrayResource resource = new ByteArrayResource(chunk);

        return new FileChunkResponse(resource, chunk);
    }

    @Override
    public FileInfoResponse getFileInfoById(Integer id) {
        Attachment attachment = getById(id);
        String mimeType = mimeDetector.deetectMimeType(attachment.getBlob());
        return new FileInfoResponse(attachment.getName(), (long) attachment.getBlob().length, mimeType);
    }

    @Override
    public void replaceAttachmentsByTicketId(Integer ticketId, List<Attachment> attachmentList){
        attachmentRepository.deleteAllByTicketId(ticketId);
        attachmentRepository.saveAll(attachmentList);
    }
}
