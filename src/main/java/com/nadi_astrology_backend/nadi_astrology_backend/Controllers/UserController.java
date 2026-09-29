package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.UserRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    // ==========================================
    // Register User
    // ==========================================

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(
            @Valid @RequestBody UserRequest request
    ) {

        UserResponse response =
                userService.registerUserByEmail(request);

        ApiResponse<UserResponse> apiResponse =
                ApiResponse.<UserResponse>builder()
                        .error(false)
                        .status(HttpStatus.CREATED.value())
                        .message("User registered successfully")
                        .payload(response)
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    // ==========================================
    // Get Current User
    // ==========================================

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            Authentication authentication
    ) {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) authentication.getPrincipal();

        assert authenticatedUser != null;
        UserResponse response =
                userService.getUserById(
                        authenticatedUser.getUserId()
                );

        ApiResponse<UserResponse> apiResponse =
                ApiResponse.<UserResponse>builder()
                        .error(false)
                        .status(HttpStatus.OK.value())
                        .message("Current user fetched successfully")
                        .payload(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    // ==========================================
    // Get User By ID
    // ==========================================

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable
            @Positive(message = "User ID must be greater than 0")
            Long userId
    ) {

        UserResponse response =
                userService.getUserById(userId);

        ApiResponse<UserResponse> apiResponse =
                ApiResponse.<UserResponse>builder()
                        .error(false)
                        .status(HttpStatus.OK.value())
                        .message("User fetched successfully")
                        .payload(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}