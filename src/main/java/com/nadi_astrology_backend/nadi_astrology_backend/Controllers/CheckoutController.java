package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CheckoutRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.OrderResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;


    // ============================================================
    // CREATE CHECKOUT
    // ============================================================

    @PostMapping
    public ResponseEntity<OrderResponse> createCheckout(
            @Valid @RequestBody CheckoutRequest request
    ) {

        OrderResponse response =
                checkoutService.createCheckout(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // GET MY ORDERS
    // ============================================================

    @GetMapping("/my-orders")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size

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

        return ResponseEntity.ok(
                checkoutService.getMyOrders(pageable)
        );
    }


    // ============================================================
    // GET MY ORDER BY ID
    // ============================================================

    @GetMapping("/my-orders/{orderId}")
    public ResponseEntity<OrderResponse> getMyOrder(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                checkoutService.getMyOrder(orderId)
        );
    }
}