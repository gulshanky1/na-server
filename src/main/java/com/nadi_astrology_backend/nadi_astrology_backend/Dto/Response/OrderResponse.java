package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;

    private String orderNumber;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
}