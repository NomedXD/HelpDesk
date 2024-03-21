package com.innowise.services;

import com.innowise.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByIdService(Integer id);
}
