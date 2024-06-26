package com.innowise.services;

import com.innowise.dto.request.UpdateTicketStatusRequest;
import com.innowise.dto.request.CreateTicketRequest;
import com.innowise.dto.request.UpdateTicketRequest;
import com.innowise.dto.response.TicketResponse;
import com.innowise.domain.Ticket;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    TicketResponse save(@Valid CreateTicketRequest request, String contextUserName);

    List<TicketResponse> findAll();

    List<TicketResponse> findAllByAssigneeEmail(String email);

    TicketResponse update(@Valid UpdateTicketRequest request, String contextUserName);

    void delete(Integer id);

    TicketResponse findById(Integer id);

    TicketResponse updateStatus(@Valid UpdateTicketStatusRequest request, String contextUserName);

    Optional<Ticket> findByIdService(Integer id);

    boolean existsByIdService(Integer id);
}
