package com.innowise.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LoginFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final AntPathRequestMatcher loginRequestMatcher = new AntPathRequestMatcher("/auth/login", HttpMethod.POST.name());
    private final BasicAuthenticationConverter basicAuthenticationConverter = new BasicAuthenticationConverter();

    public LoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(loginRequestMatcher.matches(request)) {
            UsernamePasswordAuthenticationToken authenticationToken = basicAuthenticationConverter.convert(request);
            if(authenticationToken != null) {
                SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authenticationToken));
            }
        }

        filterChain.doFilter(request, response);
    }
}
