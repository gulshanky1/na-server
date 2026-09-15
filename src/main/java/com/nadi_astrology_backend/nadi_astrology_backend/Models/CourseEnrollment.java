package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "course_enrollments",
        indexes = {
                @Index(
                        name = "idx_enrollment_student_id",
                        columnList = "student_id"
                ),
                @Index(
                        name = "idx_enrollment_course_id",
                        columnList = "course_id"
                ),
                @Index(
                        name = "idx_enrollment_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_enrollment_order_id",
                        columnList = "order_id"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_course",
                        columnNames = {
                                "student_id",
                                "course_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;


    // ============================================================
    // STUDENT
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "student_id",
            nullable = false
    )
    private Student student;


    // ============================================================
    // COURSE
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_id",
            nullable = false
    )
    private Course course;


    // ============================================================
    // ORDER
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;


    // ============================================================
    // ENROLLMENT STATUS
    // ============================================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    @Builder.Default
    private EnrollmentStatus status =
            EnrollmentStatus.ACTIVE;


    // ============================================================
    // DATES
    // ============================================================

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime enrolledAt;

    private LocalDateTime completedAt;

    private LocalDateTime expiresAt;


    // ============================================================
    // TIMESTAMPS
    // ============================================================

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // ============================================================
    // JPA LIFECYCLE
    // ============================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        enrolledAt = now;
        updatedAt = now;
    }


    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}