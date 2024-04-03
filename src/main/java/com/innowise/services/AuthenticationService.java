package com.innowise.services;

import com.innowise.dto.request.RegistrationRequest;
import com.innowise.dto.response.UserResponse;

public interface AuthenticationService {
    UserResponse register(RegistrationRequest request);
}
