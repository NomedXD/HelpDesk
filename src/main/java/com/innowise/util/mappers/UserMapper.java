package com.innowise.util.mappers;

import com.innowise.domain.User;
import com.innowise.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
