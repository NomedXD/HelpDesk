package com.innowise.repo;

import com.innowise.domain.History;

import java.util.List;

public interface HistoryRepository {
    History findById(Long id);
    List<History> findAll();

    History save(History history);
    History update(History history);

    void delete(Long id);
}
