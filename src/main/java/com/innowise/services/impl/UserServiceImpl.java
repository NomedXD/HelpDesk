package com.innowise.services.impl;

import com.innowise.domain.User;
import com.innowise.repositories.UserRepository;
import com.innowise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByIdService(Integer id) {
        return userRepository.findById(id);
    }
}
