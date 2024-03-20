package com.innowise.repo.impl;

import com.innowise.domain.History;
import com.innowise.repo.HistoryRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HistoryRepositoryImpl implements HistoryRepository {
    @PersistenceContext
    private Session session;

    @Override
    public Optional<History> findById(Integer id) {
        return Optional.ofNullable(session.find(History.class, id));
    }

    @Override
    public List<History> findAll() {
        return session.createQuery("From History", History.class).list();
    }

    @Override
    public History save(History history) {
        return session.merge(history);
    }

    @Override
    public History update(History history) {
        return session.merge(history);
    }

    @Override
    public void delete(Integer id) {
        Optional<History> historyToDelete = Optional.ofNullable(session.find(History.class, id));
        if (historyToDelete.isEmpty()) {
            //todo throw exception
        }

        session.remove(historyToDelete);
    }
}
