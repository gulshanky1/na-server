package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "orders",
        indexes = {

                // Search orders by user
                @Index(
                        name = "idx_order_user_id",
                        columnList = "user_id"
                ),

                // Search orders by status
                @Index(
                        name = "idx_order_status",
                        columnList = "status"
                ),

                // Search/sort orders by creation date
                @Index(
                        name = "idx_order_created_at",
                        columnList = "created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    // ============================================================
    // ORDER ID
    // ============================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;


    // ============================================================
    // ORDER NUMBER
    // ============================================================

    /*
     * Human-readable unique order number.
     *
     * Example:
     * NAD-20260911-000001
     *
     * This is different from orderId.
     *
     * orderId:
     *     Database internal ID
     *
     * orderNumber:
     *     Customer-facing order number
     */
    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String orderNumber;


    // ============================================================
    // USER
    // ============================================================

    /*
     * The user who placed this order.
     *
     * One User can have many Orders.
     *
     * User
     *   |
     *   | 1 : N
     *   |
     * Order
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;


    // ============================================================
    // ORDER ITEMS
    // ============================================================

    /*
     * Products purchased in this order.
     *
     * One Order can contain multiple OrderItems.
     *
     * Example:
     *
     * Order
     *   |
     *   ├── OrderItem → Course
     *   ├── OrderItem → Book
     *   └── OrderItem → Consultation
     *
     * Cascade:
     * Saving Order also saves its OrderItems.
     *
     * orphanRemoval:
     * Removing an item from the Order removes it
     * from the database.
     */
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();


    // ============================================================
    // TOTAL AMOUNT
    // ============================================================

    /*
     * Final total amount of the order.
     *
     * IMPORTANT:
     * This value must be calculated by the backend.
     *
     * Frontend should NOT send the total amount.
     */
    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal totalAmount;


    // ============================================================
    // ORDER STATUS
    // ============================================================

    /*
     * Current status of the order.
     *
     * Example flow:
     *
     * CREATED
     *     ↓
     * PAYMENT_PENDING
     *     ↓
     * PAID
     *
     * Or:
     *
     * PAYMENT_PENDING
     *     ↓
     * FAILED
     */
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    @Builder.Default
    private OrderStatus status = OrderStatus.CREATED;


    // ============================================================
    // CREATED AT
    // ============================================================

    /*
     * When the order was created.
     *
     * This value should never change.
     */
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    // ============================================================
    // UPDATED AT
    // ============================================================

    /*
     * Last time the order was updated.
     */
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;


    // ============================================================
    // JPA LIFECYCLE
    // ============================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }


    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}