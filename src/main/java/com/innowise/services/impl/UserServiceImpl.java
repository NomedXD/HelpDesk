package com.innowise.services.impl;

import com.innowise.domain.User;
import com.innowise.dto.request.ChangePasswordRequest;
import com.innowise.dto.request.UpdateUserRequest;
import com.innowise.dto.response.UserResponse;
import com.innowise.exceptions.NoSuchUserIdException;
import com.innowise.repositories.UserRepository;
import com.innowise.services.UserService;
import com.innowise.util.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Override
    public UserResponse findById(Integer id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserIdException(id)));
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
                .orElseThrow(() -> new UsernameNotFoundException("TODO custom exception"));
    }

    @Override
    public User getUserFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("TODO custom exception"));
        }
        return null;
        // some shit code IMHO, will refactor later
    }

    @Override
    public User update(UpdateUserRequest request) {
        Integer id = getUserFromPrincipal().getId();
        User userToBeUpdated = userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException
                        ("TODO custom exception"));
        userToBeUpdated.setEmail(request.email());
        userToBeUpdated.setFirstName(request.firstName());
        userToBeUpdated.setLastName(request.lastName());
        return userRepository.save(userToBeUpdated);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = getUserFromPrincipal();

        if (!encoder.matches(request.currentPassword(), user.getPassword())){
            throw new IllegalArgumentException("TODO custom exception");
        }
        if (!request.newPassword().equals(request.confirmationPassword())){
            throw new IllegalArgumentException("TODO custom exception");
        }
        user.setPassword(encoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public void delete(String email) {
        userRepository.delete(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
