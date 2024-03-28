package com.innowise.security;

import com.innowise.repositories.TokenRepository;
import com.innowise.security.entities.Token;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Instant;

//parse Authorisation into UserDetail object, check is Token available
public class TokenAuthenticationUserDetailsService implements AuthenticationUserDetailsService<
        PreAuthenticatedAuthenticationToken> {

    private final TokenRepository tokenRepository;

    public TokenAuthenticationUserDetailsService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            return new User(token.subject(), "nopassword", true, true,
                    this.tokenRepository.isAvailable(token.id()) &&
                            token.expiresAt().isAfter(Instant.now()),
                    true,
                    token.authorities().stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList());
        }

        throw new UsernameNotFoundException("Principal must me of type Token");
    }
}
