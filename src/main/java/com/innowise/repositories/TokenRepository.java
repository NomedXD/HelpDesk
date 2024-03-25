package com.innowise.repositories;

import com.innowise.domain.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    List<Token> findAllValidTokensByUserId(Integer userId);

    Optional<Token> findByToken(String token);

    void save(Token token);

    void saveAll(List<Token> tokens);
}
