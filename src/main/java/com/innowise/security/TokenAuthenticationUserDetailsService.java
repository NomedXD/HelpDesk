package com.innowise.security;

import com.innowise.domain.User;
import com.innowise.domain.UserRole;
import com.innowise.repositories.TokenRepository;
import com.innowise.security.entities.Token;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
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

            return User.builder()
                    .id(token.userId())
                    .email(token.subject())
                    .password("nopassword")
                    .role(UserRole.valueOf(token.authorities().get(0)))
                    .token(token)
                    .isAccountNonExpired(true)
                    .isEnabled(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(
                            this.tokenRepository.isExists(token.id()) &&
                            token.expiresAt().isAfter(Instant.now())
                    ).build();
        }

        throw new UsernameNotFoundException("Principal must me of type Token");
    }
}
