package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceFulfillmentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerServiceFulfillmentResponse {

    private Long fulfillmentId;

    private Long orderItemId;

    private Long orderId;

    private String orderNumber;

    private Long productId;

    private String serviceName;

    private ServiceType serviceType;

    private BigDecimal amount;

    private ServiceFulfillmentStatus status;

    private String report;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}