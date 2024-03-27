package com.innowise.controllers;

import com.innowise.dto.request.ChangeEmailRequest;
import com.innowise.dto.request.ChangePasswordRequest;
import com.innowise.dto.request.UpdateUserRequest;
import com.innowise.dto.response.UserResponse;
import com.innowise.services.UserService;
import com.innowise.util.mappers.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(@AuthenticationPrincipal UserDetails userDetails) {
        String response = userDetails.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getProfile(
            @PathVariable("username") String email) {
        UserResponse response = userService.findByEmail(email);
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/whoami")
    public ResponseEntity<String> whoami(@AuthenticationPrincipal UserDetails userDetails) {
        String response = userDetails.getUsername();
        return ResponseEntity.ok()
                .body(response);
    }

    @PatchMapping("/edit")
    public ResponseEntity<UserResponse> editProfile(@RequestBody UpdateUserRequest request) {
        UserResponse response = userMapper
                .toUserResponse(userService.update(request));
        return ResponseEntity.accepted()
                .body(response);
    }

    @PatchMapping("/edit/email")
    public ResponseEntity<String> changeEmail(@RequestBody ChangeEmailRequest request,
                                              HttpServletRequest httpRequest) {
        String token = userService.changeEmail(request, httpRequest);
        return ResponseEntity.accepted()
                .body(token);
    }

    @PatchMapping("/edit/password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.accepted()
                .body("password changed successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProfile(@RequestParam("email") String email) {
        userService.delete(email);
        return ResponseEntity.ok()
                .body("profile deleted successfully");
    }

}
