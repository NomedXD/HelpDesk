package com.innowise.repo;

import com.innowise.domain.History;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository {
    Optional<History> findById(Integer id);
    List<History> findAll();

    History save(History history);
    History update(History history);

    void delete(Integer id);
}
