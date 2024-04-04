package com.innowise.services.impl;

import com.innowise.domain.User;
import com.innowise.exceptions.UserNotFoundException;
import com.innowise.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void loadUserByUsername_withValidUsername_returnsUserDetails() {
        String userName = "test@example.com";
        User expectedUser = User.builder().email(userName).build();

        Mockito.when(userRepository.findByEmail(userName)).thenReturn(Optional.of(expectedUser));
        UserDetails actualUser = userDetailsService.loadUserByUsername(userName);

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userName);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getUsername());
    }

    @Test
    public void findById_withInvalidUserId_throwsNoSuchEntityIdException() {
        String invalidUserEmail = "invalidUser@example.com";

        Mockito.when(userRepository.findByEmail(invalidUserEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userDetailsService.loadUserByUsername(invalidUserEmail));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(invalidUserEmail);
    }
}
