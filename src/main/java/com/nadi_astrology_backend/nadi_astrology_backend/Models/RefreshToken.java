package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "refresh_token",
        indexes = {
                @Index(
                        name = "idx_refresh_token_hash",
                        columnList = "tokenHash"
                ),
                @Index(
                        name = "idx_refresh_token_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_refresh_token_family",
                        columnList = "tokenFamily"
                ),
                @Index(
                        name = "idx_refresh_token_expires_at",
                        columnList = "expiresAt"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(
            nullable = false,
            unique = true,
            length = 64
    )
    private String tokenHash;

    @Column(
            nullable = false,
            length = 36
    )
    private String tokenFamily;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime revokedAt;

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}