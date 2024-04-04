package com.innowise.security.config;

import com.innowise.repositories.TokenRepository;
import com.innowise.repositories.UserRepository;
import com.innowise.security.*;
import com.innowise.security.filters.GetCsrfTokenFilter;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public AccessTokenJwsStringSerializer accessTokenJwsStringSerializer(
            @Value("${jwt.cookie-token-key}") String signKey
    ) throws Exception {
        return new AccessTokenJwsStringSerializer(new MACSigner(OctetSequenceKey.parse(signKey)), JWSAlgorithm.HS256);
    }

    @Bean
    public TokenAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
            @Value("${jwt.cookie-token-key}") String cookieTokenKey,
            TokenRepository tokenRepository,
            UserRepository userRepository
    ) throws Exception {
        return new TokenAuthenticationConfigurer()
                .accessTokenFactory(new UserAccessTokenFactory())
                .refreshTokenFactory(new DefaultCookieTokenFactory())
                .accessTokenSerializer(new AccessTokenJwsStringSerializer(
                        new MACSigner(OctetSequenceKey.parse(cookieTokenKey))
                ))
                .userRepository(userRepository)
                .tokenRepository(tokenRepository)
                .accessTokenStringDeserializer(new AccessTokenJwsStringDeserializer(
                        new MACVerifier(OctetSequenceKey.parse(cookieTokenKey))
                ));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            TokenAuthenticationConfigurer tokenAuthenticationConfigurer,
            AccessTokenJwsStringSerializer accessTokenJwsStringSerializer,
            AuthenticationProvider authenticationProvider,
            TokenRepository tokenRepository) throws Exception
    {
        CorsFilter corsFilter = getCorsFilter(); // CORS
        TokenCookieSessionAuthenticationStrategy tokenCookieSessionAuthenticationStrategy =
                new TokenCookieSessionAuthenticationStrategy(tokenRepository);
        tokenCookieSessionAuthenticationStrategy
                .setAccessTokenStringSerializer(accessTokenJwsStringSerializer);

        http
                .httpBasic(basic -> basic.disable())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class) // CORS
                .addFilterBefore(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/error", "static/index.html").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionAuthenticationStrategy(tokenCookieSessionAuthenticationStrategy))
                .csrf(csrf -> csrf.disable());
//                        csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//                        .sessionAuthenticationStrategy((authentication, request, response) -> {}));

        http.with(tokenAuthenticationConfigurer, Customizer.withDefaults());

        return http.build();
    }

    private static CorsFilter getCorsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
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
