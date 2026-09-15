package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.GoogleAuthRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.LoginRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AuthResponse;
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


        return ResponseEntity.ok(
                apiResponse
        );
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


        return ResponseEntity.ok(
                apiResponse
        );
    }
}
