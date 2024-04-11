package com.innowise.security.config;

import com.innowise.domain.User;
import com.innowise.repositories.TokenRepository;
import com.innowise.repositories.UserRepository;
import com.innowise.security.BearerAuthenticationConverter;
import com.innowise.security.TokenAuthenticationUserDetailsService;
import com.innowise.security.entities.RefreshToken;
import com.innowise.security.entities.Token;
import com.innowise.security.filters.BearerAuthenticationFilter;
import com.innowise.security.filters.LoginFilter;
import com.innowise.security.filters.TokenRefreshFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

public class TokenAuthenticationConfigurer
        extends AbstractHttpConfigurer<TokenAuthenticationConfigurer, HttpSecurity> {

    private Function<Token, String> accessTokenSerializer;
    private Function<String, Token> accessTokenStringDeserializer;

    private TokenRepository tokenRepository;
    private UserRepository userRepository;

    private Function<User, Token> accessTokenFactory;
    private Function<Token, RefreshToken> refreshTokenFactory;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        builder.logout(logout -> logout.addLogoutHandler(
                        new CookieClearingLogoutHandler("__Host-auth-token"))
                .addLogoutHandler((request, response, authentication) -> {
                    Arrays.stream(request.getCookies())
                            .filter(cookie -> cookie.getName().equals("__Host-auth-token"))
                            .findFirst()
                            .ifPresent(refreshCookie -> this.tokenRepository.delete(UUID.fromString(refreshCookie.getValue())));
                })
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", HttpMethod.POST.name()))
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                })
        );
    }

    @Override
    public void configure(HttpSecurity builder) {
        var bearerAuthenticationFilter = new BearerAuthenticationFilter(
                builder.getSharedObject(AuthenticationManager.class),
                new BearerAuthenticationConverter(this.accessTokenStringDeserializer));

        var loginFilter = new LoginFilter(builder.getSharedObject(AuthenticationManager.class));

        var tokenRefreshFilter = new TokenRefreshFilter(this.tokenRepository, this.userRepository);
        tokenRefreshFilter.setAccessTokenSerializer(this.accessTokenSerializer);
        tokenRefreshFilter.setAccessTokenFactory(this.accessTokenFactory);
        tokenRefreshFilter.setRefreshTokenFactory(this.refreshTokenFactory);

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                new TokenAuthenticationUserDetailsService(this.tokenRepository));

        builder.addFilterAfter(loginFilter, CsrfFilter.class)
                .addFilterAfter(tokenRefreshFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(bearerAuthenticationFilter, TokenRefreshFilter.class)
                .authenticationProvider(authenticationProvider);
    }

    public TokenAuthenticationConfigurer tokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        return this;
    }

    public TokenAuthenticationConfigurer accessTokenSerializer(Function<Token, String> accessTokenSerializer) {
        this.accessTokenSerializer = accessTokenSerializer;
        return this;
    }

    public TokenAuthenticationConfigurer accessTokenStringDeserializer(Function<String, Token> accessTokenStringDeserializer) {
        this.accessTokenStringDeserializer = accessTokenStringDeserializer;
        return this;
    }

    public TokenAuthenticationConfigurer userRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }

    public TokenAuthenticationConfigurer accessTokenFactory(Function<User, Token> accessTokenFactory) {
        this.accessTokenFactory = accessTokenFactory;
        return this;
    }

    public TokenAuthenticationConfigurer refreshTokenFactory(Function<Token, RefreshToken> refreshTokenFactory) {
        this.refreshTokenFactory = refreshTokenFactory;
        return this;
    }
}