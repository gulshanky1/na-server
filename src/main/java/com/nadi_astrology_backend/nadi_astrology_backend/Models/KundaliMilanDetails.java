package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "kundali_milan_details",
        indexes = {
                @Index(
                        name = "idx_kundali_order_item_id",
                        columnList = "order_item_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KundaliMilanDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kundaliMilanId;


    // ============================================================
    // ORDER ITEM
    // ============================================================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_item_id",
            nullable = false,
            unique = true
    )
    private OrderItem orderItem;


    // ============================================================
    // BOY DETAILS
    // ============================================================

    @Column(name = "boy_name", nullable = false, length = 150)
    private String boyName;

    @Column(name = "boy_date_of_birth", nullable = false)
    private LocalDate boyDateOfBirth;

    @Column(name = "boy_birth_time", nullable = false)
    private LocalTime boyBirthTime;

    @Column(name = "boy_birth_place", nullable = false, length = 150)
    private String boyBirthPlace;


    // ============================================================
    // GIRL DETAILS
    // ============================================================

    @Column(name = "girl_name", nullable = false, length = 150)
    private String girlName;

    @Column(name = "girl_date_of_birth", nullable = false)
    private LocalDate girlDateOfBirth;

    @Column(name = "girl_birth_time", nullable = false)
    private LocalTime girlBirthTime;

    @Column(name = "girl_birth_place", nullable = false, length = 150)
    private String girlBirthPlace;


    // ============================================================
    // TIMESTAMPS
    // ============================================================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // ============================================================
    // PRE-PERSIST
    // ============================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }


    // ============================================================
    // PRE-UPDATE
    // ============================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}