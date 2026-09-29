package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ServiceFulfillmentUpdateRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ServiceFulfillmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.ServiceFulfillmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/service-fulfillments")
@RequiredArgsConstructor
@Validated
public class AdminServiceFulfillmentController {

    private final ServiceFulfillmentService serviceFulfillmentService;

    // ================================
    // GET ALL SERVICE FULFILLMENTS
    // ================================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ServiceFulfillmentResponse>>> getAllFulfillments(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size cannot exceed 50")
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ServiceFulfillmentResponse> fulfillments =
                serviceFulfillmentService.getAllFulfillments(pageable);

        ApiResponse<Page<ServiceFulfillmentResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fulfillments fetched successfully",
                        fulfillments
                );

        return ResponseEntity.ok(response);
    }

    // ================================
    // GET FULFILLMENT BY ID
    // ================================
    @GetMapping("/{fulfillmentId}")
    public ResponseEntity<ApiResponse<ServiceFulfillmentResponse>> getFulfillmentById(
            @PathVariable
            @Positive(message = "Fulfillment ID must be greater than 0")
            Long fulfillmentId
    ) {

        ServiceFulfillmentResponse fulfillment =
                serviceFulfillmentService.getFulfillmentById(
                        fulfillmentId
                );

        ApiResponse<ServiceFulfillmentResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fulfillment fetched successfully",
                        fulfillment
                );

        return ResponseEntity.ok(response);
    }

    // ================================
    // UPDATE FULFILLMENT
    // ================================
    @PutMapping("/{fulfillmentId}")
    public ResponseEntity<ApiResponse<ServiceFulfillmentResponse>> updateFulfillment(
            @PathVariable
            @Positive(message = "Fulfillment ID must be greater than 0")
            Long fulfillmentId,

            @Valid
            @RequestBody
            ServiceFulfillmentUpdateRequest request
    ) {

        ServiceFulfillmentResponse fulfillment =
                serviceFulfillmentService.updateFulfillment(
                        fulfillmentId,
                        request
                );

        ApiResponse<ServiceFulfillmentResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fulfillment updated successfully",
                        fulfillment
                );

        return ResponseEntity.ok(response);
    }
}