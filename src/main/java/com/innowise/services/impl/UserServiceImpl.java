package com.innowise.services.impl;

import com.innowise.domain.User;
import com.innowise.exceptions.NoSuchUserIdException;
import com.innowise.repositories.UserRepository;
import com.innowise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchUserIdException(id));
    }
}
