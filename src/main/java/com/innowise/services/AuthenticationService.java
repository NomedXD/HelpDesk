package com.innowise.services;

import com.innowise.domain.User;
import com.innowise.dto.request.LoginRequest;
import com.innowise.dto.request.RegistrationRequest;
import com.innowise.dto.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    ResponseEntity<?> register(RegistrationRequest request);

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest);

    void saveToken(Authentication authentication, String token);

    void saveToken(User user, String jwtToken);
}