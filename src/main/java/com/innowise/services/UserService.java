package com.innowise.services;

import com.innowise.domain.User;
import com.innowise.dto.request.ChangeEmailRequest;
import com.innowise.dto.request.ChangePasswordRequest;
import com.innowise.dto.request.UpdateUserRequest;
import com.innowise.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    User save(User user);

    UserResponse findByEmail(String email);

    UserResponse findById(Integer id);

    List<User> findAll();

    User getUserFromPrincipal();

    User findByEmailService(String email);

    User update(UpdateUserRequest request);

    void changePassword(ChangePasswordRequest request);

    String changeEmail(ChangeEmailRequest request, HttpServletRequest httpRequest);

    void delete(String email);

    Boolean existsByEmail(String email);
}
