package com.innowise.security.config;

import com.innowise.repositories.TokenRepository;
import com.innowise.repositories.UserRepository;
import com.innowise.security.TokenCookieJweStringDeserializer;
import com.innowise.security.TokenCookieJweStringSerializer;
import com.innowise.security.TokenCookieSessionAuthenticationStrategy;
import com.innowise.security.filters.GetCsrfTokenFilter;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public TokenCookieJweStringSerializer tokenCookieJweStringSerializer(
            @Value("${jwt.cookie-token-key}") String cookieTokenKey
    ) throws Exception {
        return new TokenCookieJweStringSerializer(new DirectEncrypter(
                OctetSequenceKey.parse(cookieTokenKey)
        ));
    }

    @Bean
    public TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
            @Value("${jwt.cookie-token-key}") String cookieTokenKey,
            TokenRepository tokenRepository
    ) throws Exception {
        return new TokenCookieAuthenticationConfigurer()
                .tokenCookieStringDeserializer(new TokenCookieJweStringDeserializer(
                        new DirectDecrypter(
                                OctetSequenceKey.parse(cookieTokenKey)
                        )
                ))
                .tokenRepository(tokenRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer,
            TokenCookieJweStringSerializer tokenCookieJweStringSerializer,
            AuthenticationProvider authenticationProvider) throws Exception
    {
        TokenCookieSessionAuthenticationStrategy tokenCookieSessionAuthenticationStrategy =
                new TokenCookieSessionAuthenticationStrategy();
        tokenCookieSessionAuthenticationStrategy
                .setTokenStringSerializer(tokenCookieJweStringSerializer);

        http
                .httpBasic(basic ->
                        basic.authenticationEntryPoint((request, response, authException) -> {
                            if (request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                            }
                        }))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("api/auth/register").permitAll()
                                .requestMatchers("/error", "index.html").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionAuthenticationStrategy(tokenCookieSessionAuthenticationStrategy))
                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .sessionAuthenticationStrategy((authentication, request, response) -> {}));

        http.with(tokenCookieAuthenticationConfigurer, Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(email -> userRepository.findByEmail(email)
                .orElse(null));

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
