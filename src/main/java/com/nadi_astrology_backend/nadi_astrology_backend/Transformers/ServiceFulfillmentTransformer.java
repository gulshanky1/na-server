package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ServiceFulfillmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.ServiceFulfillment;
import org.springframework.stereotype.Component;

@Component
public class ServiceFulfillmentTransformer {

    public ServiceFulfillmentResponse toResponse(
            ServiceFulfillment fulfillment
    ) {

        OrderItem orderItem = fulfillment.getOrderItem();

        Order order = orderItem.getOrder();

        Product product = orderItem.getProduct();

        return ServiceFulfillmentResponse.builder()

                // Fulfillment
                .fulfillmentId(fulfillment.getFulfillmentId())
                .orderItemId(orderItem.getOrderItemId())

                // Order
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())

                // Customer
                .userId(order.getUser().getUserId())
                .customerName(order.getUser().getFullName())
                .customerEmail(order.getUser().getEmail())
                .customerPhone(order.getUser().getPhone())

                // Product / Service
                .productId(product != null
                        ? product.getProductId()
                        : null)

                .serviceName(product != null
                        ? product.getName()
                        : null)

                .serviceType(fulfillment.getServiceType())

                // Amount
                .amount(orderItem.getTotalPrice())

                // Fulfillment
                .status(fulfillment.getStatus())

                // Report
                .report(fulfillment.getReport())

                // Admin internal notes
                .adminNotes(fulfillment.getAdminNotes())

                // Dates
                .completedAt(fulfillment.getCompletedAt())
                .createdAt(fulfillment.getCreatedAt())
                .updatedAt(fulfillment.getUpdatedAt())

                .build();
    }
}