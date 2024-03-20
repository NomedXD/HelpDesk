package com.innowise.repo;

import com.innowise.domain.Ticket;

import java.util.List;

public interface TicketRepository {
    Ticket findById(Integer id);
    List<Ticket> findAll();

    Ticket save(Ticket ticket);
    Ticket update(Ticket ticket);

    void delete(Integer id);
}
