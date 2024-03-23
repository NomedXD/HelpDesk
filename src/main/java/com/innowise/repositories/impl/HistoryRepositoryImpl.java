package com.innowise.repositories.impl;

import com.innowise.domain.History;
import com.innowise.repositories.HistoryRepository;
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
    public History save(History history) {
        return session.merge(history);
    }

    @Override
    public List<History> findAll() {
        return session.createQuery("FROM History", History.class).list();
    }

    @Override
    public History update(History history) {
        return session.merge(history);
    }

    @Override
    public void delete(Integer id) {
        session.createQuery("DELETE History WHERE id = :id", History.class)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Optional<History> findById(Integer id) {
        return Optional.ofNullable(session.find(History.class, id));
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public List<History> findAllByTicketId(Integer ticketId) {
        return session.createQuery("FROM Feedback WHERE ticket.id = :id ORDER BY date DESC", History.class)
                .setParameter("id", ticketId)
                .list();
    }
}
