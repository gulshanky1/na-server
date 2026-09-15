package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.EnrollmentStatus;
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
public class CourseEnrollmentResponse {

    private Long enrollmentId;

    // Student
    private Long studentId;
    private String studentCode;

    // Course
    private Long courseId;
    private String courseTitle;
    private String courseImageUrl;
    private BigDecimal coursePrice;
    private String duration;

    // Course schedule
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String classDays;

    // Order
    private Long orderId;
    private String orderNumber;

    // Enrollment
    private EnrollmentStatus status;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime updatedAt;
}