package com.innowise.security;

import com.innowise.domain.User;
import com.innowise.security.entities.Token;
import org.springframework.security.core.GrantedAuthority;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

public class UserAccessTokenFactory implements Function<User, Token> {
    private Duration tokenTtl = Duration.ofDays(5);

    @Override
    public Token apply(User user) {
        return new Token(
                UUID.randomUUID(),
                user.getId(),
                user.getEmail(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).toList(),
                Instant.now(),
                Instant.now().plus(tokenTtl)
        );
    }
}
