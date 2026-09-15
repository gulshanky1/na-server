package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CustomerServiceFulfillmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.ServiceFulfillment;
import org.springframework.stereotype.Component;

@Component
public class CustomerServiceFulfillmentTransformer {

    public CustomerServiceFulfillmentResponse toResponse(
            ServiceFulfillment fulfillment
    ) {

        OrderItem orderItem = fulfillment.getOrderItem();

        Order order = orderItem.getOrder();

        Product product = orderItem.getProduct();

        return CustomerServiceFulfillmentResponse.builder()

                // Fulfillment
                .fulfillmentId(
                        fulfillment.getFulfillmentId()
                )

                .orderItemId(
                        orderItem.getOrderItemId()
                )

                // Order
                .orderId(
                        order.getOrderId()
                )

                .orderNumber(
                        order.getOrderNumber()
                )

                // Product / Service
                .productId(
                        product != null
                                ? product.getProductId()
                                : null
                )

                .serviceName(
                        product != null
                                ? product.getName()
                                : null
                )

                .serviceType(
                        fulfillment.getServiceType()
                )

                // Amount
                .amount(
                        orderItem.getTotalPrice()
                )

                // Status
                .status(
                        fulfillment.getStatus()
                )

                // Customer report
                .report(
                        fulfillment.getReport()
                )

                // Dates
                .completedAt(
                        fulfillment.getCompletedAt()
                )

                .createdAt(
                        fulfillment.getCreatedAt()
                )

                .updatedAt(
                        fulfillment.getUpdatedAt()
                )

                .build();
    }
}