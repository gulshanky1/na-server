package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "order_items",
        indexes = {
                @Index(
                        name = "idx_order_item_order_id",
                        columnList = "order_id"
                ),
                @Index(
                        name = "idx_order_item_product_id",
                        columnList = "product_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    /*
     * Which order does this item belong to?
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;

    /*
     * Which product was purchased?
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;

    /*
     * Snapshot of product name at purchase time.
     */
    @Column(nullable = false, length = 200)
    private String productName;

    /*
     * Snapshot of product price at purchase time.
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    /*
     * Quantity purchased.
     */
    @Column(nullable = false)
    private Integer quantity;

    /*
     * unitPrice × quantity
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;
}