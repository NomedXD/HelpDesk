package com.innowise.controllers;

import com.innowise.domain.enums.TicketState;
import com.innowise.domain.enums.UserRole;
import com.innowise.dto.request.ChangeEmailRequest;
import com.innowise.dto.request.ChangePasswordRequest;
import com.innowise.dto.request.UpdateUserRequest;
import com.innowise.dto.response.UserInfoResponse;
import com.innowise.dto.response.UserResponse;
import com.innowise.services.UserService;
import com.innowise.util.mappers.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PatchMapping("/edit")
    public ResponseEntity<UserResponse> editProfile(@RequestBody UpdateUserRequest request,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userMapper.toUserResponse(userService.update(request, userDetails.getUsername()));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PatchMapping("/edit/email")
    public ResponseEntity<String> changeEmail(@RequestBody ChangeEmailRequest request,
                                              HttpServletRequest httpRequest) {
        String token = userService.changeEmail(request, httpRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
    }

    @PatchMapping("/edit/password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        userService.changePassword(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("password changed successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProfile(@AuthenticationPrincipal UserDetails userDetails) {
        userService.delete(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("profile deleted successfully");
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponse> getUserRoleActions(@AuthenticationPrincipal UserDetails userDetails) {
        UserRole role = (UserRole) userDetails.getAuthorities().iterator().next();
        String email = userDetails.getUsername();
        Map<TicketState, List<TicketState>> actions =
                ((UserRole) userDetails.getAuthorities()
                        .iterator()
                        .next())
                        .getFromToStateAuthorities();

        return ResponseEntity.ok(new UserInfoResponse(email, role, actions));
    }

}
