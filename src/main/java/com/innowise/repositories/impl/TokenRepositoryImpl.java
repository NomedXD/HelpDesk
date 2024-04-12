package com.innowise.repositories.impl;

import com.innowise.security.entities.RefreshToken;
import com.innowise.repositories.TokenRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public class TokenRepositoryImpl implements TokenRepository {
    @PersistenceContext
    private Session session;

    @Override
    public boolean isExists(UUID tokenId) {
        return session.createNativeQuery("SELECT COUNT(*) FROM refresh_tokens WHERE id = :id", Integer.class)
                .setParameter("id", tokenId)
                .getSingleResult() == 1;
    }

    @Override
    public boolean isUserAvailable(Integer userId) {
        return session.createNativeQuery("SELECT COUNT(*) FROM refresh_tokens WHERE user_id = :userId", Integer.class)
                .setParameter("userId", userId)
                .getSingleResult() == 0;
    }

    @Override
    public void saveToken(RefreshToken refreshToken) {
        session.persist(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findById(UUID id) {
        return Optional.ofNullable(session.find(RefreshToken.class, id));
    }

    @Override
    public void delete(UUID tokenId) {
        session.createMutationQuery("DELETE FROM RefreshToken WHERE id = :id")
                .setParameter("id", tokenId)
                .executeUpdate();
    }

    @Override
    public void deleteByUserId(Integer userId) {
        session.createMutationQuery("DELETE FROM RefreshToken WHERE userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void replace(RefreshToken newToken, Integer userId) {
        session.createMutationQuery("DELETE FROM RefreshToken WHERE userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();

        session.persist(newToken);
    }
}
