package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.UserRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // ==========================================
    // Register User
    // ==========================================

    @PostMapping("/register")

    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRequest request) {

        UserResponse response =
                userService.registerUserByEmail(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(
            Authentication authentication) {

        AuthenticatedUser user =
                (AuthenticatedUser) authentication.getPrincipal();

        return ResponseEntity.ok(
                "User ID: " + user.getUserId()
                        + ", Email: " + user.getEmail()
                        + ", Role: " + user.getRole()
        );
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                userService.getUserById(userId)
        );
    }
}