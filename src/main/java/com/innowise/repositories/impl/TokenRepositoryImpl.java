package com.innowise.repositories.impl;

import com.innowise.domain.Token;
import com.innowise.repositories.TokenRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepositoryImpl implements TokenRepository {
    @PersistenceContext
    private Session session;

    public List<Token> findAllValidTokensByUserId(Integer userId) {
        String sql = "SELECT t.* FROM tokens t INNER JOIN users u ON t.user_id = u.id " +
                "WHERE u.id = :userId AND (t.expired = false OR t.revoked = false)";
        NativeQuery<Token> query = session.createNativeQuery(sql, Token.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Optional<Token> findByToken(String token) {
        String sql = "SELECT t.* FROM tokens t WHERE t.token = :token";
        NativeQuery<Token> query = session.createNativeQuery(sql, Token.class);
        query.setParameter("token", token);
        return Optional.ofNullable(query.uniqueResult());
    }

    @Override
    public void save(Token token) {
        session.merge(token);
    }

    @Override
    public void saveAll(List<Token> tokens) {
        for (Token token : tokens) {
            session.merge(token);
        }
    }


}
