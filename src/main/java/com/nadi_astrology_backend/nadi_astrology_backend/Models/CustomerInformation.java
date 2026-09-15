package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "customer_information",
        indexes = {
                @Index(
                        name = "idx_customer_info_order_id",
                        columnList = "order_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerInformationId;


    // ============================================================
    // ORDER
    // ============================================================

    /*
     * This information belongs to a particular purchase.
     *
     * It is a snapshot of customer information at checkout time.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            unique = true
    )
    private Order order;


    // ============================================================
    // BASIC CONTACT INFORMATION
    // ============================================================

    @Column(
            nullable = false,
            length = 150
    )
    private String fullName;

    @Column(
            nullable = false,
            length = 150
    )
    private String email;

    @Column(
            nullable = false,
            length = 20
    )
    private String phone;


    // ============================================================
    // ASTROLOGY INFORMATION
    // ============================================================

    private LocalDate dateOfBirth;

    private LocalTime birthTime;

    @Column(length = 150)
    private String birthPlace;

    @Column(length = 100)
    private String countryOfBirth;


    // ============================================================
    // ADDRESS
    // ============================================================

    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 100)
    private String currentLivingCountry;


    // ============================================================
    // QUESTIONS
    // ============================================================

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String comments;


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