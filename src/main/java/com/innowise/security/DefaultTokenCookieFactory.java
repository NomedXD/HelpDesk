package com.innowise.security;

import com.innowise.domain.User;
import com.innowise.security.entities.Token;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Setter
public class DefaultTokenCookieFactory implements Function<Authentication, Token> {

    private Duration tokenTtl = Duration.ofDays(1);

    @Override
    public Token apply(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();
        return new Token(UUID.randomUUID(),
                user.getId(),
                authentication.getName(),
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList(),
                now, now.plus(tokenTtl));
    }

}
