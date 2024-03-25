package com.innowise.repositories;

import com.innowise.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    List<User> findAll();

    User update(User user);

    void delete(String email);

    boolean existsById(Integer id);

    boolean existsByEmail(String email);

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

}
