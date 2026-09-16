package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminUserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Role;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<AdminUserResponse>> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean accountEnabled,
            @RequestParam(required = false) Boolean student,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
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

        return ResponseEntity.ok(
                userService.getAllUsers(
                        search,
                        role,
                        accountEnabled,
                        student,
                        pageable
                )
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AdminUserResponse> getUser(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                userService.getAdminUserById(userId)
        );
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<AdminUserResponse> updateAccountStatus(

            @PathVariable Long userId,

            @RequestParam boolean enabled
    ) {

        return ResponseEntity.ok(
                userService.updateAccountStatus(
                        userId,
                        enabled
                )
        );
    }
}