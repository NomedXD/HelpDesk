package com.innowise.repo;

import com.innowise.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Integer id);
    List<User> findAll();

    User save(User user);
    User update(User user);

    void delete(Integer id);
}
