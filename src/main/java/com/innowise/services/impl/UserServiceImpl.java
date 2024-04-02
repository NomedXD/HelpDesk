package com.innowise.services.impl;

import com.innowise.domain.User;
import com.innowise.dto.request.ChangeEmailRequest;
import com.innowise.dto.request.ChangePasswordRequest;
import com.innowise.dto.request.UpdateUserRequest;
import com.innowise.dto.response.UserResponse;
import com.innowise.exceptions.EntityTypeMessages;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.exceptions.UserNotFoundException;
import com.innowise.exceptions.WrongConfirmedPasswordException;
import com.innowise.exceptions.WrongCurrentPasswordException;
import com.innowise.repositories.UserRepository;
import com.innowise.services.AuthenticationService;
import com.innowise.services.JwtService;
import com.innowise.services.UserService;
import com.innowise.util.mappers.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private AuthenticationService authenticationService;

    @Lazy
    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public UserResponse findById(Integer id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityIdException(EntityTypeMessages.USER_MESSAGE, id)));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserResponse findByEmail(String email) {
        return userMapper.toUserResponse(findByEmailService(email));
    }

    @Override
    public User findByEmailService(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User getUserFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new UserNotFoundException(authentication.getName()));
        }
        return null;
        // some shit code IMHO, will refactor later
    }

    @Override
    public User update(UpdateUserRequest request) {
        Integer id = getUserFromPrincipal().getId();
        User userToBeUpdated = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchEntityIdException(EntityTypeMessages.USER_MESSAGE, id));
        userToBeUpdated.setFirstName(request.firstName());
        userToBeUpdated.setLastName(request.lastName());
        return userRepository.save(userToBeUpdated);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = getUserFromPrincipal();

        if (!encoder.matches(request.currentPassword(), user.getPassword())) {
            throw new WrongCurrentPasswordException();
        }
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new WrongConfirmedPasswordException();
        }
        user.setPassword(encoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public String changeEmail(ChangeEmailRequest request, HttpServletRequest httpRequest) {
        User user = getUserFromPrincipal();
        user.setEmail(request.email());
        String token = jwtService.getTokenFromRequest(httpRequest);
        Integer userId = jwtService.getUserIdFromToken(token);
        String newToken = jwtService.generateToken(userId, request.email());
        authenticationService.saveToken(user, newToken);
        save(user);
        return newToken;
    }
    // a little bit messy, maybe refactor later

    @Override
    public void delete(String email) {
        userRepository.delete(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
