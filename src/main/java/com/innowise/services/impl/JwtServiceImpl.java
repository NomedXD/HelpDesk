package com.innowise.services.impl;

import com.innowise.domain.Token;
import com.innowise.repositories.TokenRepository;
import com.innowise.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Log
@Service
@Transactional
public class JwtServiceImpl implements JwtService {
    private final TokenRepository tokenRepository;
    private final Key key;
    @Autowired
    public JwtServiceImpl(TokenRepository tokenRepository,
                          @Value("${jwt.secret}") String keyString) {
        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA512");
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String generateToken(int id, String username) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + 1000 * 60 * 24);

        return Jwts.builder()
                .claim("user_id", id)
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    @Override
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("user_id", Integer.class);
    }

    @Override
    public Optional<Token> getByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public Boolean validateToken(String token) {
        var isTokenValid = tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
        if (!isTokenValid) return false;

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.severe("Expired or invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Token> getAllValidTokensByUserId(Integer id) {
        return tokenRepository.findAllValidTokensByUserId(id);
    }

    @Override
    public void saveJwt(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public void saveJwts(List<Token> tokens) {
        tokenRepository.saveAll(tokens);
    }

    @Override
    public void revokeAllUserTokens(int id){
        var validTokens = getAllValidTokensByUserId(id);
        if (validTokens.isEmpty()) return;
        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        saveJwts(validTokens);
    }
}
