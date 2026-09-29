package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminOrderResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminOrderResponse>>> getAllOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.DESC,
                                "createdAt"
                        )
                );

        Page<AdminOrderResponse> orders =
                adminOrderService.getAllOrders(
                        search,
                        status,
                        pageable
                );

        ApiResponse<Page<AdminOrderResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Orders fetched successfully",
                        orders
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<AdminOrderResponse>> getOrder(
            @PathVariable Long orderId
    ) {

        AdminOrderResponse order =
                adminOrderService.getOrderById(orderId);

        ApiResponse<AdminOrderResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Order fetched successfully",
                        order
                );

        return ResponseEntity.ok(response);
    }
}