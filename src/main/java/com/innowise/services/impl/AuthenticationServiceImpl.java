package com.innowise.services.impl;

import com.innowise.domain.Token;
import com.innowise.domain.User;
import com.innowise.domain.enums.TokenType;
import com.innowise.domain.enums.UserRole;
import com.innowise.dto.request.LoginRequest;
import com.innowise.dto.request.RegistrationRequest;
import com.innowise.dto.response.LoginResponse;
import com.innowise.dto.response.UserResponse;
import com.innowise.exceptions.UserAlreadyExistsException;
import com.innowise.services.AuthenticationService;
import com.innowise.services.JwtService;
import com.innowise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse register(RegistrationRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(encodedPassword).role(UserRole.ROLE_EMPLOYEE)
                .build();
        User savedUser = userService.save(user);
        String jwtToken = jwtService.generateToken(savedUser.getId(), savedUser.getUsername());

        saveToken(savedUser, jwtToken);
        return new LoginResponse(jwtToken);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserResponse user = userService.findByEmail(authentication.getName());
        String token = jwtService.generateToken(user.id(), user.email());
        revokeAllUserTokens(authentication);
        saveToken(authentication, token);

        return new LoginResponse(token);
    }

    @Override
    public void saveToken(Authentication authentication, String token) {
        User user = userService.findByEmailService(authentication.getName());
        saveToken(user, token);
    }

    @Override
    public void saveToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        jwtService.saveJwt(token);
    }

    public void revokeAllUserTokens(Authentication authentication) {
        Integer id = userService
                .findByEmailService(authentication.getName())
                .getId();
        jwtService.revokeAllUserTokens(id);
    }
}
