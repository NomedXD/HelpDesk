package com.innowise.repositories.impl;

import com.innowise.domain.UserRole;
import com.innowise.repositories.UserRoleRepository;
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
    public UserRole save(UserRole userRole) {
        return session.merge(userRole);
    }

    @Override
    public List<UserRole> findAll() {
        return session.createQuery("FROM UserRole", UserRole.class).list();
    }

    @Override
    public UserRole update(UserRole userRole) {
        return session.merge(userRole);
    }

    @Override
    public void delete(Integer id) {
        session.createMutationQuery("DELETE FROM UserRole WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();    }

    @Override
    public boolean existsById(Integer id) {
        return session.createNativeQuery("SELECT COUNT(*) FROM user_role WHERE id = :id", Integer.class)
                .setParameter("id", id)
                .getSingleResult() == 1;
    }

    @Override
    public Optional<UserRole> findById(Integer id) {
        return Optional.ofNullable(session.find(UserRole.class, id));
    }
}
