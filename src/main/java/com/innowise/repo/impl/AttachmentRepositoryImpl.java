package com.innowise.repo.impl;

import com.innowise.domain.Attachment;
import com.innowise.repo.AttachmentRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AttachmentRepositoryImpl implements AttachmentRepository {
    @PersistenceContext
    private Session session;

    @Override
    public Attachment findById(Integer id) {
        return session.find(Attachment.class, id);
    }

    @Override
    public List<Attachment> findAll() {
        return session.createQuery("From Attachment", Attachment.class).list();
    }

    @Override
    public Attachment save(Attachment attachment) {
        return session.merge(attachment);
    }

    @Override
    public Attachment update(Attachment attachment) {
        return session.merge(attachment);
    }

    @Override
    public void delete(Integer id) {
        Optional<Attachment> attachmentToDelete = Optional.of(session.find(Attachment.class, id));
        if (attachmentToDelete.isEmpty()) {
            //todo throw exception
        }

        session.remove(attachmentToDelete);
    }
}
