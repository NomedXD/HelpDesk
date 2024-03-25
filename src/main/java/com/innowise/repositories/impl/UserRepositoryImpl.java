package com.innowise.repositories.impl;

import com.innowise.domain.User;
import com.innowise.repositories.UserRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private Session session;

    @Override
    public User save(User user) {
        return session.merge(user);
    }

    @Override
    public List<User> findAll() {
        return session.createQuery("FROM User", User.class).list();
    }

    @Override
    public User update(User user) {
        return session.merge(user);
    }

    @Override
    public void delete(Integer id) {
        session.createMutationQuery("DELETE FROM User WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();    }

    @Override
    public boolean existsById(Integer id) {
        return session.createNativeQuery("SELECT COUNT(*) FROM users WHERE id = :id", Integer.class)
                .setParameter("id", id)
                .getSingleResult() == 1;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(session.find(User.class, id));
    }
}
