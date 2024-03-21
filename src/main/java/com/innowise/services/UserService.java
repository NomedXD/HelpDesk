package com.innowise.services;

import com.innowise.domain.User;

import java.util.Optional;

public interface UserService {
    User findById(Integer id);
}
