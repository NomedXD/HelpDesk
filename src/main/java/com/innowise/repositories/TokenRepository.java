package com.innowise.repositories;

import com.innowise.security.entities.Token;

import java.util.UUID;

public interface TokenRepository {
    boolean isAvailable(UUID tokenId);
    void blockToken(Token token);
}
