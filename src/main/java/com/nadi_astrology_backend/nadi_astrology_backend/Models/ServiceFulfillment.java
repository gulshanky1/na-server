package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceFulfillmentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "service_fulfillments",
        indexes = {

                @Index(
                        name = "idx_service_fulfillment_order_item",
                        columnList = "order_item_id"
                ),

                @Index(
                        name = "idx_service_fulfillment_status",
                        columnList = "status"
                ),

                @Index(
                        name = "idx_service_fulfillment_service_type",
                        columnList = "service_type"
                ),

                @Index(
                        name = "idx_service_fulfillment_created_at",
                        columnList = "created_at"
                )
        },
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_service_fulfillment_order_item",
                        columnNames = "order_item_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceFulfillment {

    // ============================================================
    // ID
    // ============================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fulfillmentId;


    // ============================================================
    // ORDER ITEM
    // ============================================================

    /*
     * The exact service purchased by the customer.
     *
     * Example:
     *
     * Order #5
     *      |
     *      └── OrderItem #5
     *               |
     *               └── Product #7
     *                        |
     *                        └── Kundali Milan
     *
     * ServiceFulfillment
     *      |
     *      └── OrderItem #5
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_item_id",
            nullable = false,
            unique = true
    )
    private OrderItem orderItem;


    // ============================================================
    // SERVICE TYPE
    // ============================================================

    /*
     * Snapshot of the service type at the time
     * fulfillment was created.
     *
     * Example:
     *
     * KUNDALI_MILAN
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "service_type",
            nullable = false,
            length = 50
    )
    private ServiceType serviceType;


    // ============================================================
    // FULFILLMENT STATUS
    // ============================================================

    /*
     * PENDING
     *      ↓
     * IN_PROGRESS
     *      ↓
     * COMPLETED
     *
     * Or:
     *
     * PENDING → CANCELLED
     */
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    @Builder.Default
    private ServiceFulfillmentStatus status =
            ServiceFulfillmentStatus.PENDING;


    // ============================================================
    // REPORT
    // ============================================================

    /*
     * Report written by admin.
     *
     * TEXT is used because astrology reports
     * can become very large.
     *
     * Example:
     *
     * "According to the planetary analysis..."
     */
    @Column(
            columnDefinition = "TEXT"
    )
    private String report;


    // ============================================================
    // ADMIN NOTES
    // ============================================================

    /*
     * Internal notes for admin.
     *
     * Customer should NOT see this.
     */
    @Column(
            name = "admin_notes",
            columnDefinition = "TEXT"
    )
    private String adminNotes;


    // ============================================================
    // COMPLETED AT
    // ============================================================

    /*
     * Set when admin completes the service.
     */
    @Column(
            name = "completed_at"
    )
    private LocalDateTime completedAt;


    // ============================================================
    // CREATED AT
    // ============================================================

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    // ============================================================
    // UPDATED AT
    // ============================================================

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;


    // ============================================================
    // PRE-PERSIST
    // ============================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = ServiceFulfillmentStatus.PENDING;
        }
    }


    // ============================================================
    // PRE-UPDATE
    // ============================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}