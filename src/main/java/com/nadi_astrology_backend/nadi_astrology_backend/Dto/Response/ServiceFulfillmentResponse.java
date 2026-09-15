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
public class ServiceFulfillmentResponse {

    // ============================================================
    // FULFILLMENT
    // ============================================================

    private Long fulfillmentId;

    private Long orderItemId;

    // ============================================================
    // ORDER
    // ============================================================

    private Long orderId;

    private String orderNumber;

    // ============================================================
    // CUSTOMER
    // ============================================================

    private Long userId;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    // ============================================================
    // SERVICE
    // ============================================================

    private Long productId;

    private String serviceName;

    private ServiceType serviceType;

    private BigDecimal amount;

    // ============================================================
    // FULFILLMENT STATUS
    // ============================================================

    private ServiceFulfillmentStatus status;

    // ============================================================
    // REPORT
    // ============================================================

    /*
     * Report written by admin.
     *
     * This is visible to the customer
     * when the service is completed.
     */
    private String report;

    // ============================================================
    // ADMIN NOTES
    // ============================================================

    /*
     * Internal notes.
     *
     * We will NOT expose this to customer APIs later.
     */
    private String adminNotes;

    // ============================================================
    // TIMESTAMPS
    // ============================================================

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}