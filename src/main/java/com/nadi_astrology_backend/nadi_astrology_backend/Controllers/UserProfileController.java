package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.UserProfileRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserProfileResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(
            Authentication authentication
    ) {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) authentication.getPrincipal();

        Long userId =
                authenticatedUser.getUserId();

        return ResponseEntity.ok(
                userProfileService.getMyProfile(userId)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> createOrUpdateProfile(
            @Valid @RequestBody UserProfileRequest request,
            Authentication authentication
    ) {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) authentication.getPrincipal();

        Long userId =
                authenticatedUser.getUserId();

        return ResponseEntity.ok(
                userProfileService.createOrUpdateProfile(
                        userId,
                        request
                )
        );
    }
}