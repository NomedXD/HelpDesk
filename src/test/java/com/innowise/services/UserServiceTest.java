package com.innowise.services;

import com.innowise.domain.User;
import com.innowise.domain.enums.UserRole;
import com.innowise.dto.request.UpdateUserRequest;
import com.innowise.dto.response.UserResponse;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.exceptions.UserNotFoundException;
import com.innowise.repositories.UserRepository;
import com.innowise.services.impl.UserServiceImpl;
import com.innowise.util.mappers.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void save_withValidUser_savesUser() {
        User savedUser = User.builder()
                .email("test")
                .firstName("test")
                .lastName("test")
                .password("test").role(UserRole.ROLE_EMPLOYEE)
                .build();
        User expectedUser = User.builder().id((int) (Math.random() * 10) + 1).build();
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);
        User user = userService.save(savedUser);

        Mockito.verify(userRepository, Mockito.times(1)).save(savedUser);
        Assertions.assertNull(savedUser.getId());
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void findById_withValidUserId_returnsUser() {
        Integer userId = 1;
        User expectedUser = User.builder().id(userId).build();

        Mockito.when(userRepository.findById(Mockito.argThat(argument -> (argument != null) && (argument >= 1)))).
                thenReturn(Optional.of(expectedUser));
        UserResponse userResponse = userService.findById(userId);

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Assertions.assertNotNull(userResponse);
        Assertions.assertEquals(expectedUser.getId(), userResponse.id());
    }

    @Test
    public void findById_withInvalidUserId_throwsNoSuchEntityIdException() {
        Integer invalidUserId = 999;

        Mockito.when(userRepository.findById(Mockito.argThat(argument -> (argument != null) && (argument >= 1)))).
                thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchEntityIdException.class, () -> userService.findById(invalidUserId));
        Mockito.verify(userRepository, Mockito.times(1)).findById(invalidUserId);
    }

    @Test
    public void findAll_withValid_returnsUserList() {
        List<User> expectedUserList = List.of(User.builder().id(1).build(), User.builder().id(2).build());

        Mockito.when(userRepository.findAll()).thenReturn(expectedUserList);
        List<User> actualUsers = userService.findAll();

        Assertions.assertEquals(expectedUserList, actualUsers);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findByEmail_withValidEmail_returnsUser() {
        String email = "test@example.com";
        User user = User.builder().id(1).email(email).build();
        UserResponse expectedUserResponse = UserResponse.builder().id(1).email(email).build();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserResponse actualUserResponse = userService.findByEmail(email);

        Assertions.assertEquals(expectedUserResponse, actualUserResponse);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
    }

    @Test
    public void findByEmail_withInvalidEmail_throwsUserNotFoundException() {
        String invalidEmail = "invalid_email";

        Mockito.when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findByEmail(invalidEmail));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(invalidEmail);
    }

    // TODO refactor getUserFromPrincipal method *to ycovich*
    @Test
    public void update_withValidUserId_returnsUpdatedUser() {
        Integer userId = 1;
        String updatedFirstName = "Updated First Name";
        String updatedLastName = "Updated Last Name";
        UpdateUserRequest request = UpdateUserRequest.builder().firstName(updatedFirstName).lastName(updatedLastName).build();

        User existingUser = User.builder().id(userId).firstName("Current first name").lastName("Current last name").build();
        User updatedUser = User.builder().id(userId).firstName(updatedFirstName).lastName(updatedLastName).build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.update(request);

        Assertions.assertEquals(updatedUser, result);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepository, Mockito.times(1)).save(updatedUser);
    }

    @Test
    public void delete_withValidUserEmail_deletesUser() {
        String email = "test@example.com";

        Mockito.doNothing().when(userRepository).delete(email);
        userService.delete(email);

        Mockito.verify(userRepository, Mockito.times(1)).delete(email);
    }

    @Test
    public void existsByEmail_withValidEmail_returnsTrue() {
        String email = "test@example.com";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);
        boolean result = userService.existsByEmail(email);

        Assertions.assertTrue(result);
    }

    @Test
    public void existsByEmail_withInvalidEmail_returnsFalse() {
        String email = "invalid@example.com";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        boolean result = userService.existsByEmail(email);

        Assertions.assertFalse(result);
    }
}
