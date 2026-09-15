package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ConsultationType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "phone_consultations",
        indexes = {
                @Index(
                        name = "idx_phone_consultation_type",
                        columnList = "consultation_type"
                ),
                @Index(
                        name = "idx_phone_consultation_active",
                        columnList = "active"
                ),
                @Index(
                        name = "idx_phone_consultation_name",
                        columnList = "name"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneConsultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultationId;


    // ============================================================
    // CONSULTATION INFORMATION
    // ============================================================

    @Column(nullable = false, length = 200)
    private String name;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "consultation_type",
            nullable = false,
            length = 50
    )
    private ConsultationType consultationType;


    @Column(length = 500)
    private String shortDescription;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;


    // ============================================================
    // IMAGE
    // ============================================================

    @Column(length = 500)
    private String imageUrl;


    // ============================================================
    // CONSULTATION DURATION
    // ============================================================

    @Column(nullable = false)
    private Integer durationMinutes;


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

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // ============================================================
    // CREATE TIMESTAMP
    // ============================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }


    // ============================================================
    // UPDATE TIMESTAMP
    // ============================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}