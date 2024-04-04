package com.innowise.services.impl;

import com.innowise.domain.User;
import com.innowise.domain.enums.UserRole;
import com.innowise.dto.request.ChangePasswordRequest;
import com.innowise.dto.request.UpdateUserRequest;
import com.innowise.dto.response.UserResponse;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.exceptions.UserNotFoundException;
import com.innowise.exceptions.WrongConfirmedPasswordException;
import com.innowise.exceptions.WrongCurrentPasswordException;
import com.innowise.repositories.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Spy
    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    public void save_withValidUser_savesUser() {
        String email = "test@example.com";
        String firstName = "First name";
        String lastName = "Last name";
        String password = "password";
        User savedUser = User.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password).role(UserRole.ROLE_EMPLOYEE)
                .build();
        User expectedUser = User.builder().id((int) (Math.random() * 10) + 1).build();
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);
        User actualUser = userService.save(savedUser);

        Mockito.verify(userRepository, Mockito.times(1)).save(savedUser);
        Assertions.assertNull(savedUser.getId());
        Assertions.assertNotNull(actualUser.getId());
    }

    @Test
    public void findById_withValidUserId_returnsUser() {
        Integer userId = 1;
        User expectedUser = User.builder().id(userId).build();

        Mockito.when(userRepository.findById(Mockito.argThat(argument -> (argument != null) && (argument >= 1)))).
                thenReturn(Optional.of(expectedUser));
        UserResponse actualUserResponse = userService.findById(userId);

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Assertions.assertNotNull(actualUserResponse);
        Assertions.assertEquals(expectedUser.getId(), actualUserResponse.id());
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
        List<User> actualUserList = userService.findAll();

        Assertions.assertEquals(expectedUserList, actualUserList);
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
        String invalidUserEmail = "invalidUser@example.com";

        Mockito.when(userRepository.findByEmail(invalidUserEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findByEmail(invalidUserEmail));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(invalidUserEmail);
    }

    @Test
    public void update_withValidUserName_returnsUpdatedUser() {
        Integer userId = 1;
        String userEmail = "test@example.com";
        String updatedFirstName = "Updated First Name";
        String updatedLastName = "Updated Last Name";
        String currentFirstName = "Current first name";
        String currentLastName = "Current last name";
        UpdateUserRequest updatedUserRequest = UpdateUserRequest.builder().firstName(updatedFirstName).lastName(updatedLastName).build();
        User existingUser = User.builder().id(userId).firstName(currentFirstName).lastName(currentLastName).build();
        User updatedUser = User.builder().id(userId).firstName(updatedFirstName).lastName(updatedLastName).build();

        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User actualUser = userService.update(updatedUserRequest, userEmail);

        Assertions.assertEquals(updatedUser, actualUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(updatedUser);
    }

    @Test
    public void update_withInvalidUserName_throwsUserNotFoundException() {
        String invalidEmail = "invalidUser@example.com";
        String updatedFirstName = "Updated First Name";
        String updatedLastName = "Updated Last Name";
        UpdateUserRequest updatedUserRequest = UpdateUserRequest.builder().firstName(updatedFirstName).lastName(updatedLastName).build();

        Mockito.when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.update(updatedUserRequest, invalidEmail));
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(User.class));
    }

    @Test
    public void changePassword_withValidUserNameAndRequest_notThrowsExceptions() {
        String userEmail = "validUser@example.com";
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmationPassword = "newPassword";
        User existingUser = User.builder().password(currentPassword).build();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder().currentPassword(currentPassword).
                newPassword(newPassword).confirmationPassword(confirmationPassword).build();

        Mockito.when(encoder.matches(currentPassword, currentPassword)).thenReturn(true);
        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(existingUser)).thenReturn(null);

        Assertions.assertDoesNotThrow(() -> userService.changePassword(changePasswordRequest, userEmail));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userEmail);
        Mockito.verify(userRepository, Mockito.times(1)).save(existingUser);
    }

    @Test
    public void changePassword_withInvalidUserName_throwsUserNotFoundException() {
        String userEmail = "invalidUser@example.com";
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmationPassword = "newPassword";
        User existingUser = User.builder().password(currentPassword).build();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder().currentPassword(currentPassword).
                newPassword(newPassword).confirmationPassword(confirmationPassword).build();

        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.changePassword(changePasswordRequest, userEmail));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userEmail);
        Mockito.verify(userRepository, Mockito.times(0)).save(existingUser);
    }

    @Test
    public void changePassword_withInvalidCurrentPassword_throwsWrongCurrentPasswordException() {
        String userEmail = "validUser@example.com";
        String currentPassword = "currentPassword";
        String invalidCurrentPassword = "invalidCurrentPassword";
        String newPassword = "newPassword";
        String confirmationPassword = "newPassword";
        User existingUser = User.builder().password(currentPassword).build();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder().currentPassword(invalidCurrentPassword).
                newPassword(newPassword).confirmationPassword(confirmationPassword).build();

        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        Mockito.when(encoder.matches(changePasswordRequest.currentPassword(), existingUser.getPassword())).thenReturn(false);

        Assertions.assertThrows(WrongCurrentPasswordException.class, () -> userService.changePassword(changePasswordRequest, userEmail));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userEmail);
        Mockito.verify(userRepository, Mockito.times(0)).save(existingUser);
    }

    @Test
    public void changePassword_withInvalidConfirmedPassword_throwsWrongConfirmedPasswordException() {
        String userEmail = "validUser@example.com";
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmationPassword = "newInvalidPassword";
        User existingUser = User.builder().password(currentPassword).build();
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder().currentPassword(currentPassword).
                newPassword(newPassword).confirmationPassword(confirmationPassword).build();

        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        Mockito.when(encoder.matches(changePasswordRequest.currentPassword(), existingUser.getPassword())).thenReturn(true);

        Assertions.assertThrows(WrongConfirmedPasswordException.class, () -> userService.changePassword(changePasswordRequest, userEmail));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userEmail);
        Mockito.verify(userRepository, Mockito.times(0)).save(existingUser);
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
