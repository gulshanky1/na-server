package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {

    private Long courseId;

    // Basic information
    private String title;

    private String shortDescription;

    private String description;

    private String imageUrl;

    // Course information
    private String duration;

    private String syllabus;

    // Price
    private BigDecimal price;

    // Schedule
    private LocalDate startDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String classDays;

    // Status
    private boolean active;

    // Tags
    private String tags;

    // Timestamps
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}