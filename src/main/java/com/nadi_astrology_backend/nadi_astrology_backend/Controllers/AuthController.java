package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.GoogleAuthRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.LoginRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.RefreshTokenRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ResendVerificationRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AuthResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.RefreshTokenResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /*
     * ============================================================
     * LOGIN
     * ============================================================
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(request);

        ApiResponse<AuthResponse> apiResponse =
                ApiResponse.<AuthResponse>builder()
                        .error(false)
                        .status(200)
                        .message("Login successful")
                        .payload(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }


    /*
     * ============================================================
     * GOOGLE LOGIN
     * ============================================================
     */
    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthResponse>> googleLogin(
            @Valid @RequestBody GoogleAuthRequest request
    ) {

        AuthResponse response =
                authService.googleLogin(request);

        ApiResponse<AuthResponse> apiResponse =
                ApiResponse.<AuthResponse>builder()
                        .error(false)
                        .status(200)
                        .message("Google login successful")
                        .payload(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }


    /*
     * ============================================================
     * EMAIL VERIFICATION
     * ============================================================
     */
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @RequestParam String token
    ) {

        authService.verifyEmail(token);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .error(false)
                        .status(200)
                        .message("Email verified successfully")
                        .build();

        return ResponseEntity.ok(response);
    }


    /*
     * ============================================================
     * RESEND VERIFICATION EMAIL
     * ============================================================
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>> resendVerificationEmail(
            @Valid @RequestBody ResendVerificationRequest request,
            jakarta.servlet.http.HttpServletRequest httpServletRequest
    ) {

        String clientIp =
                httpServletRequest.getRemoteAddr();

        authService.resendVerificationEmail(
                request.getEmail(),
                clientIp
        );

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .error(false)
                        .status(200)
                        .message("Verification email sent successfully")
                        .build();

        return ResponseEntity.ok(response);
    }


    /*
     * ============================================================
     * REFRESH ACCESS TOKEN
     * ============================================================
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        RefreshTokenResponse response =
                authService.refreshAccessToken(
                        request.getRefreshToken()
                );

        ApiResponse<RefreshTokenResponse> apiResponse =
                ApiResponse.<RefreshTokenResponse>builder()
                        .error(false)
                        .status(200)
                        .message("Token refreshed successfully")
                        .payload(response)
                        .build();

        return ResponseEntity.ok(apiResponse);
    }


    /*
     * ============================================================
     * LOGOUT
     * ============================================================
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        authService.logout(
                request.getRefreshToken()
        );

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .error(false)
                        .status(200)
                        .message("Logout successful")
                        .build();

        return ResponseEntity.ok(response);
    }
}