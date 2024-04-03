package com.innowise.security.filters;

import com.innowise.exceptions.TokenVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BearerAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationConverter bearerAuthenticationConverter;
    private final AuthenticationManager authenticationManager;

    public BearerAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationConverter bearerAuthenticationConverter) {
        this.bearerAuthenticationConverter = bearerAuthenticationConverter;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            PreAuthenticatedAuthenticationToken authentication = (PreAuthenticatedAuthenticationToken) this.bearerAuthenticationConverter.convert(request);
            if(authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(this.authenticationManager.authenticate(authentication));
            }

        } catch (TokenVerificationException e) {
            JSONObject json = new JSONObject();
            try {
                json.put("error", e.getMessage());
                json.put("link", "/auth/login");
                response.sendError(401, json.toString());
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }

        }

        filterChain.doFilter(request, response);
    }
}
