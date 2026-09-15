package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ServiceResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ServiceResponse>>> getActiveServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ServiceResponse> services =
                serviceService.getActiveServices(pageable);

        ApiResponse<Page<ServiceResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Services fetched successfully",
                        services
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(
            @PathVariable Long serviceId
    ) {

        ServiceResponse service =
                serviceService.getServiceById(serviceId);

        ApiResponse<ServiceResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fetched successfully",
                        service
                );

        return ResponseEntity.ok(response);
    }
}