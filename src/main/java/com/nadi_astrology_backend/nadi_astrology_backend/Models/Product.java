package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(
                        name = "idx_product_type",
                        columnList = "type"
                ),
                @Index(
                        name = "idx_product_active",
                        columnList = "active"
                ),
                @Index(
                        name = "idx_product_reference",
                        columnList = "referenceId"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;


    // ============================================================
    // PRODUCT TYPE
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private ProductType type;


    // ============================================================
    // REFERENCE TO ACTUAL BUSINESS ENTITY
    // ============================================================

    /*
     * COURSE              -> courseId
     * BOOK                -> bookId
     * SERVICE             -> serviceId
     * PHONE_CONSULTATION  -> consultationId
     */
    @Column(nullable = false)
    private Long referenceId;


    // ============================================================
    // PRODUCT INFORMATION
    // ============================================================

    @Column(
            nullable = false,
            length = 200
    )
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String imageUrl;


    // ============================================================
    // PRICE
    // ============================================================

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal price;


    // ============================================================
    // STATUS
    // ============================================================

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;


    // ============================================================
    // TIMESTAMPS
    // ============================================================

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // ============================================================
    // JPA
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