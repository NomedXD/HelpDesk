package com.innowise.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.domain.User;
import com.innowise.repositories.TokenRepository;
import com.innowise.repositories.UserRepository;
import com.innowise.security.entities.RefreshToken;
import com.innowise.security.entities.Token;
import com.innowise.security.entities.TokenResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Setter
public class TokenRefreshFilter extends OncePerRequestFilter {
    private final AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/auth/refresh", HttpMethod.POST.name());
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    private Function<User, Token> accessTokenFactory;
    private Function<Token, RefreshToken> refreshTokenFactory;

    private Function<Token, String> accessTokenSerializer = Objects::toString;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String AUTH_TOKEN_COOKIE_NAME = "__Host-auth-token";

    public TokenRefreshFilter(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            Cookie refreshCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(AUTH_TOKEN_COOKIE_NAME))
                    .findFirst()
                    .orElse(null);

            if (refreshCookie != null) {
                try {
                    RefreshToken token = tokenRepository.findById(UUID.fromString(refreshCookie.getValue()))
                            .orElseThrow(() -> new AccessDeniedException("Token is not valid: " + refreshCookie.getValue()));

                    if (token.getExpiresAt().isBefore(Instant.now())) {
                        throw new CredentialsExpiredException("Token is expired");
                    }
                    // TODO add new exception to throw 403

                    User user = userRepository.findById(token.getUserId())
                            .orElseThrow(() -> {
                                log.error("User found in token = '%s' with id = %d not found!".formatted(token.getId(), token.getUserId()));
                                return new AccessDeniedException("Wrong token provided");
                            });

                    Token accessToken = accessTokenFactory.apply(user);
                    RefreshToken newRefreshToken = refreshTokenFactory.apply(accessToken);

                    String accessTokenString = accessTokenSerializer.apply(accessToken);

                    Cookie newCookie = new Cookie(AUTH_TOKEN_COOKIE_NAME, newRefreshToken.getId().toString());
                    newCookie.setPath("/");
                    newCookie.setDomain(null);
                    newCookie.setSecure(true);
                    newCookie.setHttpOnly(true);
                    newCookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), newRefreshToken.getExpiresAt()));

                    tokenRepository.replace(newRefreshToken, newRefreshToken.getUserId());

                    response.addCookie(newCookie);
                    this.objectMapper.writeValue(response.getWriter(), new TokenResponseWrapper(accessTokenString));
                    response.setStatus(HttpStatus.OK.value());

                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Token is not valid: " + refreshCookie.getValue());
                } catch (IOException e) {
                    log.error("Error while write token value!", e);
                    throw new RuntimeException("Internal error"); //todo where is internal exception?
                }

            }
        }

        filterChain.doFilter(request, response);
    }
}