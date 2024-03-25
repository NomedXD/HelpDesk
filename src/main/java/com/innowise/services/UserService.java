package com.innowise.services;

import com.innowise.domain.User;
import com.innowise.dto.request.ChangePasswordRequest;
import com.innowise.dto.request.UpdateUserRequest;
import com.innowise.dto.response.UserResponse;

public interface UserService {
    User save(User user);

    UserResponse findByEmail(String email);

    UserResponse findById(Integer id);

    User getUserFromPrincipal();

    User findByEmailService(String email);

    User update(UpdateUserRequest request);

    void changePassword(ChangePasswordRequest request);

    void delete(String email);

    Boolean existsByEmail(String email);
}
