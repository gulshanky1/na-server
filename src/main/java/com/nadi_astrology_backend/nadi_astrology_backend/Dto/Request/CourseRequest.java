package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {

    // ============================================================
    // BASIC INFORMATION
    // ============================================================

    @NotBlank(message = "Course title is required")
    @Size(
            max = 200,
            message = "Course title cannot exceed 200 characters"
    )
    private String title;


    @Size(
            max = 500,
            message = "Short description cannot exceed 500 characters"
    )
    private String shortDescription;


    @NotBlank(message = "Course description is required")
    private String description;


    private String imageUrl;


    // ============================================================
    // COURSE INFORMATION
    // ============================================================

    @NotBlank(message = "Course duration is required")
    private String duration;


    /*
     * JSON string for syllabus.
     */
    private String syllabus;


    // ============================================================
    // PRICE
    // ============================================================

    @NotNull(message = "Course price is required")
    @DecimalMin(
            value = "0.01",
            message = "Course price must be greater than 0"
    )
    private BigDecimal price;


    // ============================================================
    // SCHEDULE
    // ============================================================

    @NotNull(message = "Course start date is required")
    private LocalDate startDate;


    @NotNull(message = "Course start time is required")
    private LocalTime startTime;


    @NotNull(message = "Course end time is required")
    private LocalTime endTime;


    @NotBlank(message = "Class days are required")
    private String classDays;


    // ============================================================
    // STATUS
    // ============================================================

    /*
     * Nullable so that during update:
     *
     * null = don't change current status
     */
    private Boolean active;


    // ============================================================
    // TAGS
    // ============================================================

    /*
     * JSON string.
     */
    private String tags;
}