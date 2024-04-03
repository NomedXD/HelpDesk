package com.innowise.services.impl;

import com.innowise.domain.User;
import com.innowise.domain.enums.UserRole;
import com.innowise.dto.request.RegistrationRequest;
import com.innowise.dto.response.UserResponse;
import com.innowise.exceptions.UserAlreadyExistsException;
import com.innowise.services.AuthenticationService;
import com.innowise.services.UserService;
import com.innowise.util.mappers.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse register(RegistrationRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(encodedPassword)
                .role(UserRole.ROLE_EMPLOYEE)
                .build();

        User savedUser = userService.save(user);

        return userMapper.toUserResponse(savedUser);
    }
}
