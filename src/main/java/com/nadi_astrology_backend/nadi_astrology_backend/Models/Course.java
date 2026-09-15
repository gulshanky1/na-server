package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "courses",
        indexes = {
                @Index(
                        name = "idx_course_active",
                        columnList = "active"
                ),
                @Index(
                        name = "idx_course_start_date",
                        columnList = "startDate"
                ),
                @Index(
                        name = "idx_course_title",
                        columnList = "title"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;


    // ============================================================
    // BASIC INFORMATION
    // ============================================================

    @Column(
            nullable = false,
            length = 200
    )
    private String title;


    @Column(
            length = 500
    )
    private String shortDescription;


    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;


    /*
     * Course thumbnail / banner image.
     *
     * Example:
     * https://res.cloudinary.com/...
     */
    @Column(
            length = 500
    )
    private String imageUrl;


    // ============================================================
    // COURSE INFORMATION
    // ============================================================

    /*
     * Example:
     *
     * "6 months"
     * "3 months"
     * "5 months"
     */
    @Column(
            nullable = false,
            length = 50
    )
    private String duration;


    /*
     * Structured syllabus content.
     *
     * Example:
     *
     * [
     *   {
     *     "title": "Introduction to Nadi Astrology",
     *     "description": "..."
     *   }
     * ]
     *
     * For the first version we keep it as JSON.
     * Later, if needed, this can become a separate CourseModule table.
     */
    @Column(
            columnDefinition = "TEXT"
    )
    private String syllabus;


    // ============================================================
    // PRICING
    // ============================================================

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal price;


    // ============================================================
    // CLASS SCHEDULE
    // ============================================================

    @Column(nullable = false)
    private LocalDate startDate;


    @Column(nullable = false)
    private LocalTime startTime;


    @Column(nullable = false)
    private LocalTime endTime;


    /*
     * Example:
     *
     * "Sunday"
     * "Saturday, Sunday"
     */
    @Column(
            nullable = false,
            length = 100
    )
    private String classDays;


    // ============================================================
    // STATUS
    // ============================================================

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;


    // ============================================================
    // TAGS
    // ============================================================

    /*
     * Example:
     *
     * [
     *     "Nadi Astrology",
     *     "Predictive Astrology",
     *     "Advanced",
     *     "Online"
     * ]
     *
     * Stored as JSON text for V1.
     */
    @Column(
            columnDefinition = "TEXT"
    )
    private String tags;


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
    // JPA LIFECYCLE
    // ============================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }


    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}