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

//refresh Token object -> build new access Token object based on refresh Token
@Setter
public class DefaultAccessTokenFactory implements Function<Authentication, Token> {
    private Duration tokenTtl = Duration.ofMinutes(3);

    public Token apply(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();
        return new Token(
                UUID.randomUUID(),
                user.getId(),
                authentication.getName(),
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList(),
                now, now.plus(tokenTtl));
    }
}
