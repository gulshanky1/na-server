package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentVerifyRequest {

    @NotBlank(message = "Razorpay payment ID is required")
    @Size(max = 255, message = "Payment ID is too long")
    private String razorpayPaymentId;

    @NotBlank(message = "Razorpay order ID is required")
    @Size(max = 255, message = "Order ID is too long")
    private String razorpayOrderId;

    @NotBlank(message = "Razorpay signature is required")
    @Size(max = 255, message = "Signature is too long")
    private String razorpaySignature;
}