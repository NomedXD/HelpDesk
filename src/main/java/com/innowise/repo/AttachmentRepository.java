package com.innowise.repo;

import com.innowise.domain.Attachment;

import java.util.List;

public interface AttachmentRepository {
    Attachment findById(Integer id);
    List<Attachment> findAll();

    Attachment save(Attachment attachment);
    Attachment update(Attachment attachment);

    void delete(Integer id);
}
