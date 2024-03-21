package com.innowise.repositories;

import com.innowise.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    List<User> findAll();

    User update(User user);

    void delete(Integer id);

    boolean existsById(Integer id);

    Optional<User> findById(Integer id);

}
