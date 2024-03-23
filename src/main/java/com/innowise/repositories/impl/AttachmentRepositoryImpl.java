package com.innowise.repositories.impl;

import com.innowise.domain.Attachment;
import com.innowise.repositories.AttachmentRepository;
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
    public Attachment save(Attachment attachment) {
        return session.merge(attachment);
    }

    @Override
    public List<Attachment> findAll() {
        return session.createQuery("FROM Attachment", Attachment.class).list();
    }

    @Override
    public Attachment update(Attachment attachment) {
        return session.merge(attachment);
    }

    @Override
    public void delete(Integer id) {
        session.remove(session.find(Attachment.class, id));
    }

    @Override
    public boolean existsById(Integer id) {
        return session.createNativeQuery("SELECT COUNT(*) FROM attachment WHERE id = :id", Integer.class)
                .setParameter("id", id)
                .getSingleResult() == 1;
    }

    @Override
    public Optional<Attachment> findById(Integer id) {
        return Optional.of(session.find(Attachment.class, id));
    }
}
