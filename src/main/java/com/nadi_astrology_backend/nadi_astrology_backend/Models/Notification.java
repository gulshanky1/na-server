package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(
                        name = "idx_notification_user_id",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_notification_read",
                        columnList = "is_read"
                ),
                @Index(
                        name = "idx_notification_created_at",
                        columnList = "created_at"
                ),
                @Index(
                        name = "idx_notification_user_read_created",
                        columnList = "user_id, is_read, created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    // =====================================================
    // USER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    // =====================================================
    // NOTIFICATION DETAILS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 50
    )
    private NotificationType type;

    @Column(
            nullable = false,
            length = 200
    )
    private String title;

    @Column(
            nullable = false,
            length = 1000
    )
    private String message;

    // =====================================================
    // OPTIONAL REFERENCE
    // =====================================================

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(
            name = "reference_type",
            length = 50
    )
    private String referenceType;

    // =====================================================
    // STATUS
    // =====================================================

    @Builder.Default
    @Column(
            name = "is_read",
            nullable = false
    )
    private boolean read = false;

    // =====================================================
    // TIMESTAMP
    // =====================================================

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}