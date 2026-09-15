package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "user_profiles",
        indexes = {
                @Index(name = "idx_profile_user_id", columnList = "user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    /*
     * One User can have only one Profile.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    private LocalDate dateOfBirth;

    private LocalTime timeOfBirth;

    @Column(length = 150)
    private String placeOfBirth;

    @Column(length = 20)
    private Gender gender;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 10)
    private String pincode;

    @Column(length = 1000)
    private String bio;

    @Column(length = 500)
    private String profileImage;

    @Column(nullable = false)
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