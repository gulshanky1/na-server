package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long orderItemId;

    private Long productId;

    private String productName;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;
}