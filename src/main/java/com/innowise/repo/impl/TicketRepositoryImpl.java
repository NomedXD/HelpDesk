package com.innowise.repo.impl;

import com.innowise.domain.Ticket;
import com.innowise.repo.TicketRepository;
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
    public Ticket findById(Long id) {
        return session.find(Ticket.class, id);
    }

    @Override
    public List<Ticket> findAll() {
        return session.createQuery("From Ticket", Ticket.class).list();
    }

    @Override
    public Ticket save(Ticket ticket) {
        return session.merge(ticket);
    }

    @Override
    public Ticket update(Ticket ticket) {
        return session.merge(ticket);
    }

    @Override
    public void delete(Long id) {
        Optional<Ticket> ticketToDelete = Optional.of(session.find(Ticket.class, id));
        if (ticketToDelete.isEmpty()) {
            //todo throw exception
        }

        session.remove(ticketToDelete);
    }
}
