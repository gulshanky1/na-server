package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CustomerServiceFulfillmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.ServiceFulfillmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/service-fulfillments")
@RequiredArgsConstructor
public class CustomerServiceFulfillmentController {

    private final ServiceFulfillmentService serviceFulfillmentService;

    /**
     * Get all service fulfillments
     * belonging to the authenticated customer.
     */
    @GetMapping("/me")
    public ResponseEntity<
            ApiResponse<Page<CustomerServiceFulfillmentResponse>>
            > getMyFulfillments(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Long userId = authenticatedUser.getUserId();

        Pageable pageable = PageRequest.of(page, size);

        Page<CustomerServiceFulfillmentResponse> fulfillments =
                serviceFulfillmentService.getMyCustomerFulfillments(
                        userId,
                        pageable
                );

        ApiResponse<Page<CustomerServiceFulfillmentResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fulfillments fetched successfully",
                        fulfillments
                );

        return ResponseEntity.ok(response);
    }


    /**
     * Get one service fulfillment.
     *
     * Only the authenticated customer's own
     * fulfillment can be accessed.
     */
    @GetMapping("/{fulfillmentId}")
    public ResponseEntity<
            ApiResponse<CustomerServiceFulfillmentResponse>
            > getMyFulfillment(
            @PathVariable Long fulfillmentId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        Long userId = authenticatedUser.getUserId();

        CustomerServiceFulfillmentResponse fulfillment =
                serviceFulfillmentService.getMyCustomerFulfillment(
                        fulfillmentId,
                        userId
                );

        ApiResponse<CustomerServiceFulfillmentResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Service fulfillment fetched successfully",
                        fulfillment
                );

        return ResponseEntity.ok(response);
    }
}