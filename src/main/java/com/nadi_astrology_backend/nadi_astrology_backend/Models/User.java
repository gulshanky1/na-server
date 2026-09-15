package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.AuthProvider;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phone"),
                @Index(name = "idx_user_provider_id", columnList = "providerId")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    // =========================================================
    // PRIMARY KEY
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    // =========================================================
    // BASIC USER INFORMATION
    // =========================================================

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(unique = true, length = 15)
    private String phone;


    // =========================================================
    // AUTHENTICATION
    // =========================================================

    /**
     * Nullable because Google users don't have
     * a local password.
     *
     * Store only encoded passwords.
     */
    @Column
    private String password;


    /**
     * User role.
     *
     * Normal registration always creates USER.
     * ADMIN should be assigned through a protected
     * admin operation.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private Role role = Role.USER;


    /**
     * Authentication provider.
     *
     * LOCAL  -> Email + Password
     * GOOGLE -> Google OAuth
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;


    /**
     * Unique ID from OAuth provider.
     *
     * Google users -> Google's unique user ID
     * Local users  -> null
     */
    @Column(unique = true, length = 255)
    private String providerId;


    /**
     * Profile image URL.
     */
    @Column(length = 500)
    private String profileImage;


    // =========================================================
    // ACCOUNT STATUS
    // =========================================================

    /**
     * Whether email is verified.
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false;


    /**
     * Whether account is active.
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean accountEnabled = true;


    // =========================================================
    // TIMESTAMPS
    // =========================================================

    /**
     * Account creation time.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime registeredAt;


    /**
     * Last update time.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    /**
     * Last successful login.
     */
    private LocalDateTime lastLogin;


    // =========================================================
    // JPA LIFECYCLE
    // =========================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (registeredAt == null) {
            registeredAt = now;
        }

        updatedAt = now;
    }


    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}