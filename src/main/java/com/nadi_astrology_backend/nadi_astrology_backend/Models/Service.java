package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "services",
        indexes = {
                @Index(name = "idx_service_active", columnList = "active"),
                @Index(name = "idx_service_name", columnList = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", length = 50)
    @Builder.Default
    private ServiceType serviceType = ServiceType.STANDARD;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

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