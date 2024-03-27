package com.innowise.repositories;

import com.innowise.domain.Attachment;
import com.innowise.domain.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket save(Ticket ticket);

    List<Ticket> findAll();

    Ticket update(Ticket ticket);

    void delete(Integer id);

    boolean existsById(Integer id);

    Optional<Ticket> findById(Integer id);

    void saveAttachmetsToTicket(List<Attachment> attachmentList);
}
