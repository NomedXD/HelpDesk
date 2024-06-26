package com.innowise.repositories.impl;

import com.innowise.domain.Attachment;
import com.innowise.domain.Ticket;
import com.innowise.repositories.TicketRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TicketRepositoryImpl implements TicketRepository {
    @PersistenceContext
    private Session session;

    @Override
    public Ticket save(Ticket ticket) {
        return session.merge(ticket);
    }

    @Override
    public List<Ticket> findAll() {
        return session.createQuery("FROM Ticket", Ticket.class).list();
    }

    @Override
    public List<Ticket> findAllByAssigneeId(Integer id) {
        return session.createQuery("FROM Ticket WHERE assignee.id = :id", Ticket.class)
                .setParameter("id", id)
                .list();
    }

    @Override
    public List<Ticket> findAllByAssigneeEmail(String email) {
        return session.createQuery("FROM Ticket WHERE assignee.email = :email", Ticket.class)
                .setParameter("email", email)
                .list();
    }

    @Override
    public Ticket update(Ticket ticket) {
        return session.merge(ticket);
    }

    @Override
    public void delete(Integer id) {
        session.createMutationQuery("DELETE FROM Ticket WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(Integer id) {
        return session.createNativeQuery("SELECT COUNT(*) FROM tickets WHERE id = :id", Integer.class)
                .setParameter("id", id)
                .getSingleResult() == 1;
    }

    @Override
    public Optional<Ticket> findById(Integer id) {
        return Optional.ofNullable(session.find(Ticket.class, id));
    }

    @Override
    public void saveAttachmetsToTicket(List<Attachment> attachmentList) {
        for(Attachment attachment: attachmentList) {
            session.persist(attachment);
        }
    }
}
