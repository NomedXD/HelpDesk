package com.innowise.repo.impl;

import com.innowise.domain.UserRole;
import com.innowise.repo.UserRoleRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRoleRepositoryImpl implements UserRoleRepository {
    @PersistenceContext
    private Session session;

    @Override
    public Optional<UserRole> findById(Integer id) {
        return Optional.ofNullable(session.find(UserRole.class, id));
    }

    @Override
    public List<UserRole> findAll() {
        return session.createQuery("From UserRole", UserRole.class).list();
    }

    @Override
    public UserRole save(UserRole userRole) {
        return session.merge(userRole);
    }

    @Override
    public UserRole update(UserRole userRole) {
        return session.merge(userRole);
    }

    @Override
    public void delete(Integer id) {
        Optional<UserRole> userRoleToDelete = Optional.ofNullable(session.find(UserRole.class, id));
        if (userRoleToDelete.isEmpty()) {
            //todo throw exception
        }

        session.remove(userRoleToDelete);
    }
}
