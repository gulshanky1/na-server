package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.AccountStatusRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                adminUserService.getAllUsers(pageable)
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                adminUserService.getUserById(userId)
        );
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserResponse> updateAccountStatus(
            @PathVariable Long userId,
            @Valid @RequestBody AccountStatusRequest request,
            Authentication authentication
    ) {

        AuthenticatedUser admin =
                (AuthenticatedUser) authentication.getPrincipal();

        UserResponse response =
                adminUserService.updateAccountStatus(
                        userId,
                        request,
                        admin.getUserId()
                );

        return ResponseEntity.ok(response);
    }
}