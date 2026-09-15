package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminDashboardResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {

        AdminDashboardResponse dashboard =
                adminDashboardService.getDashboard();

        return ResponseEntity.ok(
                ApiResponse.<AdminDashboardResponse>builder()
                        .error(false)
                        .status(200)
                        .message("Admin dashboard fetched successfully")
                        .payload(dashboard)
                        .build()
        );
    }
}