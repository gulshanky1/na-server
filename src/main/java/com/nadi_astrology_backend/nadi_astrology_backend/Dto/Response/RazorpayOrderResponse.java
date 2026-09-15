package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RazorpayOrderResponse {

    private Long paymentId;

    private Long orderId;

    private String orderNumber;

    private String razorpayOrderId;

    private BigDecimal amount;

    private String currency;

    private String status;
}