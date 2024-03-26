package com.innowise.services.impl;

import com.innowise.domain.Token;
import com.innowise.domain.User;
import com.innowise.domain.UserRole;
import com.innowise.domain.enums.TokenType;
import com.innowise.dto.request.LoginRequest;
import com.innowise.dto.request.RegistrationRequest;
import com.innowise.dto.response.LoginResponse;
import com.innowise.dto.response.UserResponse;
import com.innowise.exceptions.NoSuchRoleNameException;
import com.innowise.exceptions.UserAlreadyExistsException;
import com.innowise.repositories.UserRoleRepository;
import com.innowise.services.AuthenticationService;
import com.innowise.services.JwtService;
import com.innowise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final UserRoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<?> register(RegistrationRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(encodedPassword)
                .build();
        // TODO Заменить roleName на enum и искать по id(ну либо повесить индекс по имя)
        UserRole role = roleRepository.findByName("ROLE_EMPLOYEE").
                orElseThrow(() -> new NoSuchRoleNameException("ROLE_EMPLOYEE"));
        user.setRole(role);
        User savedUser = userService.save(user);
        String jwtToken = jwtService.generateToken(savedUser.getId(), savedUser.getUsername());

        saveToken(savedUser, jwtToken);
        // TODO убрать <?> и возвращать просто строку. Убрать MediaType
        // TODO из этих сервисов тоже возвращать только dto, Response entity формировать в контроллере
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body("[successful registration]\n" + new LoginResponse(jwtToken));
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserResponse user = userService.findByEmail(authentication.getName());
        String token = jwtService.generateToken(user.id(), user.email());
        revokeAllUserTokens(authentication);
        saveToken(authentication, token);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginResponse(token));
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
