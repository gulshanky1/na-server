package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ServiceFulfillmentUpdateRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ServiceFulfillmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceFulfillmentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.ServiceFulfillmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/service-fulfillments")
@RequiredArgsConstructor
public class AdminServiceFulfillmentController {

    private final ServiceFulfillmentService serviceFulfillmentService;


    /**
     * Get all service fulfillments.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ServiceFulfillmentResponse>>>
    getAllFulfillments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
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


    /**
     * Get fulfillment by ID.
     */
    @GetMapping("/{fulfillmentId}")
    public ResponseEntity<ApiResponse<ServiceFulfillmentResponse>>
    getFulfillmentById(
            @PathVariable Long fulfillmentId
    ) {

        /*
         * Admin is allowed to view any fulfillment.
         *
         * We already have getMyFulfillment() for customers.
         * For admin we directly fetch from repository through
         * the service layer.
         */
        ServiceFulfillmentResponse fulfillment =
                serviceFulfillmentService
                        .getFulfillmentById(fulfillmentId);

        ApiResponse<ServiceFulfillmentResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fulfillment fetched successfully",
                        fulfillment
                );

        return ResponseEntity.ok(response);
    }


    /**
     * Filter fulfillments by status.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<ServiceFulfillmentResponse>>>
    getByStatus(
            @PathVariable ServiceFulfillmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ServiceFulfillmentResponse> fulfillments =
                serviceFulfillmentService
                        .getByStatus(status, pageable);

        ApiResponse<Page<ServiceFulfillmentResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fulfillments fetched successfully",
                        fulfillments
                );

        return ResponseEntity.ok(response);
    }


    /**
     * Update report, admin notes and status.
     */
    @PutMapping("/{fulfillmentId}")
    public ResponseEntity<ApiResponse<ServiceFulfillmentResponse>>
    updateFulfillment(
            @PathVariable Long fulfillmentId,
            @Valid @RequestBody ServiceFulfillmentUpdateRequest request
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


    /**
     * Start service fulfillment.
     *
     * PENDING -> IN_PROGRESS
     */
    @PostMapping("/{fulfillmentId}/start")
    public ResponseEntity<ApiResponse<ServiceFulfillmentResponse>>
    startFulfillment(
            @PathVariable Long fulfillmentId
    ) {

        ServiceFulfillmentResponse fulfillment =
                serviceFulfillmentService.startFulfillment(
                        fulfillmentId
                );

        ApiResponse<ServiceFulfillmentResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fulfillment started successfully",
                        fulfillment
                );

        return ResponseEntity.ok(response);
    }


    /**
     * Cancel service fulfillment.
     */
    @PostMapping("/{fulfillmentId}/cancel")
    public ResponseEntity<ApiResponse<ServiceFulfillmentResponse>>
    cancelFulfillment(
            @PathVariable Long fulfillmentId
    ) {

        ServiceFulfillmentResponse fulfillment =
                serviceFulfillmentService.cancelFulfillment(
                        fulfillmentId
                );

        ApiResponse<ServiceFulfillmentResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fulfillment cancelled successfully",
                        fulfillment
                );

        return ResponseEntity.ok(response);
    }
}