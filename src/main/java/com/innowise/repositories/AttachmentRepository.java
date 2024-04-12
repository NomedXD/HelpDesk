package com.innowise.repositories;

import com.innowise.domain.Attachment;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository {
    Attachment save(Attachment attachment);

    List<Attachment> saveAll(List<Attachment> attachments);

    List<Attachment> findAll();

    Attachment update(Attachment attachment);

    boolean existsById(Integer id);

    void delete(Integer id);

    void deleteAllByTicketId(Integer ticketId);

    Optional<Attachment> findById(Integer id);
}
