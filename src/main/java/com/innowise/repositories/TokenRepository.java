package com.innowise.repositories;

import com.innowise.security.entities.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository {
    boolean isExists(UUID tokenId);
    void saveToken(RefreshToken refreshToken);
    Optional<RefreshToken> findById(UUID id);

    boolean isUserAvailable(Integer userId);

    void delete(UUID tokenId);
    void deleteByUserId(Integer userId);
    void replace(RefreshToken newToken, Integer userId);
}
