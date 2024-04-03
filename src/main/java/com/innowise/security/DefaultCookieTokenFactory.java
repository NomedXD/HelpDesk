package com.innowise.security;

import com.innowise.security.entities.RefreshToken;
import com.innowise.security.entities.Token;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

@Setter
public class DefaultCookieTokenFactory implements Function<Token, RefreshToken> {

    private Duration tokenTtl = Duration.ofDays(5);

    @Override
    public RefreshToken apply(Token token) {
        return RefreshToken.builder()
                .id(token.id())
                .userId(token.userId())
                .expiresAt(Instant.now().plus(tokenTtl))
                .build();
    }

}
