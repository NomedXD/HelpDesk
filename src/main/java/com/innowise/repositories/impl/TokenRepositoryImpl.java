package com.innowise.repositories.impl;

import com.innowise.security.entities.Token;
import com.innowise.repositories.TokenRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TokenRepositoryImpl implements TokenRepository {
    @PersistenceContext
    private Session session;

    @Override
    public boolean isAvailable(UUID tokenId) {
        return session.createNativeQuery("SELECT COUNT(*) FROM tokens WHERE id = :id", Integer.class)
                .setParameter("id", tokenId)
                .getSingleResult() == 0;
    }

    @Override
    public void blockToken(Token token) {
        session.createNativeQuery("INSERT INTO tokens (id, expires_at) VALUES (:id, :expiresAt)")
                .setParameter("id", token.id())
                .setParameter("expiresAt", Date.from(token.expiresAt()))
                .executeUpdate();
    }
}
