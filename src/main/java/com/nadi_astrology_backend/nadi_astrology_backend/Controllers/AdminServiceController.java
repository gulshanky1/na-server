package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ServiceRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ServiceResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.ServiceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/services")
@RequiredArgsConstructor
@Validated
public class AdminServiceController {

    private final ServiceService serviceService;

    // ============================================================
    // CREATE SERVICE + IMAGE
    // ============================================================

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(

            @Valid @ModelAttribute ServiceRequest request,

            @RequestPart(
                    value = "file",
                    required = false
            )
            MultipartFile file
    ) {

        ServiceResponse service =
                serviceService.createService(
                        request,
                        file
                );

        ApiResponse<ServiceResponse> response =
                new ApiResponse<>(
                        false,
                        201,
                        "Service created successfully",
                        service
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // GET ALL SERVICES
    // ============================================================

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ServiceResponse>>> getAllServices(

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size cannot exceed 50")
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<ServiceResponse> services =
                serviceService.getAllServices(pageable);

        ApiResponse<Page<ServiceResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Services fetched successfully",
                        services
                );

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // GET SERVICE BY ID
    // ============================================================

    @GetMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(

            @PathVariable
            @Positive(message = "Service ID must be greater than 0")
            Long serviceId
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

    // ============================================================
    // UPDATE SERVICE
    // ============================================================

    @PutMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(

            @PathVariable
            @Positive(message = "Service ID must be greater than 0")
            Long serviceId,

            @Valid @RequestBody ServiceRequest request
    ) {

        ServiceResponse service =
                serviceService.updateService(
                        serviceId,
                        request
                );

        ApiResponse<ServiceResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service updated successfully",
                        service
                );

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // DEACTIVATE SERVICE
    // ============================================================

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> deactivateService(

            @PathVariable
            @Positive(message = "Service ID must be greater than 0")
            Long serviceId
    ) {

        ServiceResponse service =
                serviceService.deactivateService(serviceId);

        ApiResponse<ServiceResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service deactivated successfully",
                        service
                );

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // UPLOAD / CHANGE SERVICE IMAGE
    // ============================================================

    @PostMapping(
            value = "/{serviceId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<ServiceResponse>> uploadServiceImage(

            @PathVariable
            @Positive(message = "Service ID must be greater than 0")
            Long serviceId,

            @RequestPart("file")
            MultipartFile file
    ) {

        ServiceResponse service =
                serviceService.uploadServiceImage(
                        serviceId,
                        file
                );

        ApiResponse<ServiceResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service image uploaded successfully",
                        service
                );

        return ResponseEntity.ok(response);
    }
}