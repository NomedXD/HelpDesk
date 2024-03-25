package com.innowise.repositories;

import com.innowise.domain.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    UserRole save(UserRole userRole);

    List<UserRole> findAll();

    UserRole update(UserRole userRole);

    void delete(Integer id);

    boolean existsById(Integer id);

    Optional<UserRole> findById(Integer id);

    Optional<UserRole> findByName(String roleName);

}
