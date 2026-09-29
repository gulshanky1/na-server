package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminUserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Role;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {

    private final UserService userService;

    // ==========================================
    // Get All Users
    // ==========================================

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminUserResponse>>> getAllUsers(

            @RequestParam(required = false)
            @Size(
                    max = 100,
                    message = "Search cannot exceed 100 characters"
            )
            String search,

            @RequestParam(required = false)
            Role role,

            @RequestParam(required = false)
            Boolean accountEnabled,

            @RequestParam(required = false)
            Boolean student,

            @RequestParam(defaultValue = "0")
            @Min(
                    value = 0,
                    message = "Page cannot be negative"
            )
            int page,

            @RequestParam(defaultValue = "10")
            @Min(
                    value = 1,
                    message = "Page size must be at least 1"
            )
            @Max(
                    value = 50,
                    message = "Page size cannot exceed 50"
            )
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.DESC,
                                "registeredAt"
                        )
                );

        Page<AdminUserResponse> users =
                userService.getAllUsers(
                        search,
                        role,
                        accountEnabled,
                        student,
                        pageable
                );

        ApiResponse<Page<AdminUserResponse>> response =
                ApiResponse.<Page<AdminUserResponse>>builder()
                        .error(false)
                        .status(HttpStatus.OK.value())
                        .message("Users fetched successfully")
                        .payload(users)
                        .build();

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // Get User By ID
    // ==========================================

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> getUser(

            @PathVariable
            @Positive(
                    message = "User ID must be greater than 0"
            )
            Long userId
    ) {

        AdminUserResponse user =
                userService.getAdminUserById(userId);

        ApiResponse<AdminUserResponse> response =
                ApiResponse.<AdminUserResponse>builder()
                        .error(false)
                        .status(HttpStatus.OK.value())
                        .message("User fetched successfully")
                        .payload(user)
                        .build();

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // Enable / Disable User
    // ==========================================

    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<AdminUserResponse>> updateAccountStatus(

            @PathVariable
            @Positive(
                    message = "User ID must be greater than 0"
            )
            Long userId,

            @RequestParam boolean enabled
    ) {

        AdminUserResponse user =
                userService.updateAccountStatus(
                        userId,
                        enabled
                );

        ApiResponse<AdminUserResponse> response =
                ApiResponse.<AdminUserResponse>builder()
                        .error(false)
                        .status(HttpStatus.OK.value())
                        .message(
                                enabled
                                        ? "User account enabled successfully"
                                        : "User account disabled successfully"
                        )
                        .payload(user)
                        .build();

        return ResponseEntity.ok(response);
    }
}