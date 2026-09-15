package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.OrderItemResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.OrderResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OrderTransformer {

    // ============================================================
    // ORDER → ORDER RESPONSE
    // ============================================================

    public OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items =
                order.getItems() == null
                        ? Collections.emptyList()
                        : order.getItems()
                        .stream()
                        .map(this::toItemResponse)
                        .toList();

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }


    // ============================================================
    // ORDER ITEM → ORDER ITEM RESPONSE
    // ============================================================

    private OrderItemResponse toItemResponse(OrderItem item) {

        return OrderItemResponse.builder()
                .orderItemId(item.getOrderItemId())
                .productId(item.getProduct().getProductId())
                .productName(item.getProductName())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}