package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentVerifyResponse {

    private Long paymentId;

    private Long orderId;

    private String orderNumber;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private BigDecimal amount;

    private String currency;

    private PaymentStatus paymentStatus;

    private OrderStatus orderStatus;

    private String message;
}