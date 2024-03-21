package com.innowise.repositories;

import com.innowise.domain.History;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository {
    History save(History history);

    List<History> findAll();

    History update(History history);

    void delete(Integer id);

    boolean existsById(Integer id);

    Optional<History> findById(Integer id);

    List<History> findAllByTicketId(Integer ticketId);


}
