package com.innowise.services;

import com.innowise.dto.request.ChangeTicketStatusRequest;
import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.request.UpdateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.domain.Ticket;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    TicketResponse save(CreateTicketRequest request) throws IOException;

    List<TicketResponse> findAll();

    TicketResponse update(UpdateTicketRequest request);

    void delete(Integer id);

    TicketResponse findById(Integer id);

    TicketResponse updateStatus(ChangeTicketStatusRequest request);

    Optional<Ticket> findByIdService(Integer id);

    boolean existsByIdService(Integer id);
}
