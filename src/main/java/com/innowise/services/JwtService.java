package com.innowise.services;

import com.innowise.domain.Token;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface JwtService {
    String generateToken(int id, String username);

    String getTokenFromRequest(HttpServletRequest request);

    String getUsernameFromToken(String token);

    Integer getUserIdFromToken(String token);

    Boolean validateToken(String token);

    Optional<Token> getByToken(String token);

    List<Token> getAllValidTokensByUserId(Integer id);

    void saveJwt(Token token);

    void saveJwts(List<Token> tokens);

    void revokeAllUserTokens(int id);
}
