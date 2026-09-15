package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(
                        name = "idx_payment_order_id",
                        columnList = "order_id"
                ),
                @Index(
                        name = "idx_payment_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_payment_razorpay_order_id",
                        columnList = "razorpay_order_id"
                ),
                @Index(
                        name = "idx_payment_razorpay_payment_id",
                        columnList = "razorpay_payment_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;


    /*
     * Our internal Order.
     *
     * One Order can have multiple Payment attempts.
     * This is useful if a customer retries payment.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;


    /*
     * Razorpay Order ID.
     *
     * Example:
     * order_Razorpay123
     */
    @Column(
            name = "razorpay_order_id",
            nullable = false,
            unique = true,
            length = 100
    )
    private String razorpayOrderId;


    /*
     * Razorpay Payment ID.
     *
     * This is NULL when the Razorpay order
     * has been created but payment has not
     * successfully completed yet.
     */
    @Column(
            name = "razorpay_payment_id",
            unique = true,
            length = 100
    )
    private String razorpayPaymentId;


    /*
     * Signature returned by Razorpay Checkout.
     *
     * We store it for audit/debugging purposes.
     */
    @Column(
            name = "razorpay_signature",
            length = 500
    )
    private String razorpaySignature;


    /*
     * Amount stored in our database.
     *
     * Example:
     * ₹45,000 = 45000.00
     *
     * Razorpay receives:
     * 4500000 paise
     */
    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;


    /*
     * Currency used for payment.
     *
     * Currently INR.
     */
    @Column(
            nullable = false,
            length = 10
    )
    @Builder.Default
    private String currency = "INR";


    /*
     * Current payment status.
     */
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    @Builder.Default
    private PaymentStatus status = PaymentStatus.CREATED;


    /*
     * Razorpay payment method.
     *
     * Examples:
     * card
     * netbanking
     * upi
     * wallet
     */
    @Column(
            name = "payment_method",
            length = 50
    )
    private String paymentMethod;


    /*
     * Razorpay error information.
     *
     * These fields are useful when a payment fails.
     */
    @Column(
            name = "error_code",
            length = 100
    )
    private String errorCode;

    @Column(
            name = "error_description",
            length = 500
    )
    private String errorDescription;


    /*
     * Fulfillment status.
     *
     * FALSE = successful payment has not been fulfilled yet.
     * TRUE  = successful payment has already been fulfilled.
     *
     * This prevents duplicate:
     * - course enrollment
     * - notifications
     * - emails
     * - future fulfillment actions
     */
    @Column(
            name = "fulfillment_completed",
            nullable = false
    )
    @Builder.Default
    private boolean fulfillmentCompleted = false;


    /*
     * Timestamps
     */
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;


    /*
     * Set timestamps when payment is created.
     */
    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }


    /*
     * Update timestamp whenever payment is updated.
     */
    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}