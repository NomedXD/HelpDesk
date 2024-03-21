package com.innowise.repositories.impl;

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
    public Ticket update(Ticket ticket) {
        return session.merge(ticket);
    }

    @Override
    public void delete(Integer id) {
        session.remove(session.find(Ticket.class, id));
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public Optional<Ticket> findById(Integer id) {
        return Optional.ofNullable(session.find(Ticket.class, id));
    }
}
