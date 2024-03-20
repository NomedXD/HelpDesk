package com.innowise.repo.impl;

import com.innowise.domain.User;
import com.innowise.repo.UserRepository;
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
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(session.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return session.createQuery("From User", User.class).list();
    }

    @Override
    public User save(User user) {
        return session.merge(user);
    }

    @Override
    public User update(User user) {
        return session.merge(user);
    }

    @Override
    public void delete(Integer id) {
        Optional<User> userToDelete = Optional.ofNullable(session.find(User.class, id));
        if (userToDelete.isEmpty()) {
            //todo throw exception
        }

        session.remove(userToDelete);
    }
}
