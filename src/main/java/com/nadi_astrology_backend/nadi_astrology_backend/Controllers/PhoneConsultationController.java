package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.PhoneConsultationRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.PhoneConsultationResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.PhoneConsultationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
@Validated
public class PhoneConsultationController {

    private final PhoneConsultationService phoneConsultationService;

    // ================================
    // GET ALL CONSULTATIONS
    // ================================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PhoneConsultationResponse>>> getAllConsultations(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size cannot exceed 50")
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<PhoneConsultationResponse> consultations =
                phoneConsultationService.getAllConsultations(pageable);

        ApiResponse<Page<PhoneConsultationResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Consultations fetched successfully",
                        consultations
                );

        return ResponseEntity.ok(response);
    }

    // ================================
    // GET ACTIVE CONSULTATIONS
    // ================================
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<PhoneConsultationResponse>>> getActiveConsultations(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size cannot exceed 50")
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<PhoneConsultationResponse> consultations =
                phoneConsultationService.getActiveConsultations(pageable);

        ApiResponse<Page<PhoneConsultationResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Active consultations fetched successfully",
                        consultations
                );

        return ResponseEntity.ok(response);
    }

    // ================================
    // GET CONSULTATION BY ID
    // ================================
    @GetMapping("/{consultationId}")
    public ResponseEntity<ApiResponse<PhoneConsultationResponse>> getConsultationById(
            @PathVariable
            @Positive(message = "Consultation ID must be greater than 0")
            Long consultationId
    ) {

        PhoneConsultationResponse consultation =
                phoneConsultationService.getConsultationById(
                        consultationId
                );

        ApiResponse<PhoneConsultationResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Consultation fetched successfully",
                        consultation
                );

        return ResponseEntity.ok(response);
    }

    // ================================
    // CREATE CONSULTATION
    // ================================
    @PostMapping
    public ResponseEntity<ApiResponse<PhoneConsultationResponse>> createConsultation(
            @Valid
            @RequestBody
            PhoneConsultationRequest request
    ) {

        PhoneConsultationResponse consultation =
                phoneConsultationService.createConsultation(
                        request
                );

        ApiResponse<PhoneConsultationResponse> response =
                new ApiResponse<>(
                        false,
                        201,
                        "Consultation created successfully",
                        consultation
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ================================
    // UPDATE CONSULTATION
    // ================================
    @PutMapping("/{consultationId}")
    public ResponseEntity<ApiResponse<PhoneConsultationResponse>> updateConsultation(
            @PathVariable
            @Positive(message = "Consultation ID must be greater than 0")
            Long consultationId,

            @Valid
            @RequestBody
            PhoneConsultationRequest request
    ) {

        PhoneConsultationResponse consultation =
                phoneConsultationService.updateConsultation(
                        consultationId,
                        request
                );

        ApiResponse<PhoneConsultationResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Consultation updated successfully",
                        consultation
                );

        return ResponseEntity.ok(response);
    }

    // ================================
    // DELETE / DEACTIVATE CONSULTATION
    // ================================
    @DeleteMapping("/{consultationId}")
    public ResponseEntity<ApiResponse<PhoneConsultationResponse>> deactivateConsultation(
            @PathVariable
            @Positive(message = "Consultation ID must be greater than 0")
            Long consultationId
    ) {

        PhoneConsultationResponse consultation =
                phoneConsultationService.deactivateConsultation(
                        consultationId
                );

        ApiResponse<PhoneConsultationResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Consultation deactivated successfully",
                        consultation
                );

        return ResponseEntity.ok(response);
    }
}