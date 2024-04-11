package com.innowise.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.repositories.TokenRepository;
import com.innowise.security.entities.RefreshToken;
import com.innowise.security.entities.Token;
import com.innowise.security.entities.TokenResponseWrapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Function;

//set cookie after authentication
@Slf4j
@Setter
public class TokenCookieSessionAuthenticationStrategy implements SessionAuthenticationStrategy {
    private Function<Token, RefreshToken> cookieTokenFactory = new DefaultCookieTokenFactory();
    private Function<Authentication, Token> accessTokenFactory = new DefaultAccessTokenFactory();

    private Function<Token, String> accessTokenStringSerializer = Objects::toString;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TokenRepository tokenRepository;
    
    public TokenCookieSessionAuthenticationStrategy(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        if(authentication instanceof UsernamePasswordAuthenticationToken) {//only if we authenticated with Basic auth
            Token accessToken = this.accessTokenFactory.apply(authentication);
            RefreshToken refreshToken = this.cookieTokenFactory.apply(accessToken);

            this.tokenRepository.replace(refreshToken, refreshToken.getUserId());

            String accessTokenString = this.accessTokenStringSerializer.apply(accessToken);

            Cookie cookie = new Cookie("__Host-auth-token", refreshToken.getId().toString());
            cookie.setPath("/");
            cookie.setDomain(null);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), refreshToken.getExpiresAt()));

            try {
                response.addCookie(cookie);
                objectMapper.writeValue(response.getWriter(), new TokenResponseWrapper(accessTokenString));
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.OK.value());
            } catch (IOException e) {
                log.error("Error while write token value!", e);
            }
        }
    }
}
