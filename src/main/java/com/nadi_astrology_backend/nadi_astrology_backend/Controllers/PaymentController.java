package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.PaymentVerifyRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.PaymentVerifyResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.RazorpayOrderResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.PaymentService;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.RazorpayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayService razorpayService;
    private final PaymentService paymentService;

    @PostMapping("/create/{orderId}")
    public ResponseEntity<ApiResponse<RazorpayOrderResponse>> createRazorpayOrder(
            @PathVariable Long orderId
    ) {

        RazorpayOrderResponse razorpayOrder =
                razorpayService.createRazorpayOrder(orderId);

        ApiResponse<RazorpayOrderResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Razorpay order created successfully",
                        razorpayOrder
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<PaymentVerifyResponse>> verifyPayment(
            @Valid @RequestBody PaymentVerifyRequest request
    ) {

        PaymentVerifyResponse payment =
                paymentService.verifyPayment(request);

        ApiResponse<PaymentVerifyResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Payment verified successfully",
                        payment
                );

        return ResponseEntity.ok(response);
    }
}