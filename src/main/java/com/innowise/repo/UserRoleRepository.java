package com.innowise.repo;

import com.innowise.domain.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    Optional<UserRole> findById(Integer id);
    List<UserRole> findAll();

    UserRole save(UserRole userRole);
    UserRole update(UserRole userRole);

    void delete(Integer id);
}
