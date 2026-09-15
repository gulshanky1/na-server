package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CourseEnrollmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.CourseEnrollment;
import org.springframework.stereotype.Component;

@Component
public class CourseEnrollmentTransformer {


        public CourseEnrollmentResponse toResponse(
                CourseEnrollment enrollment
        ) {

                return CourseEnrollmentResponse.builder()

                        // ====================================================
                        // ENROLLMENT
                        // ====================================================

                        .enrollmentId(
                                enrollment.getEnrollmentId()
                        )


                        // ====================================================
                        // STUDENT
                        // ====================================================

                        .studentId(
                                enrollment
                                        .getStudent()
                                        .getStudentId()
                        )

                        .studentCode(
                                enrollment
                                        .getStudent()
                                        .getStudentCode()
                        )


                        // ====================================================
                        // COURSE
                        // ====================================================

                        .courseId(
                                enrollment
                                        .getCourse()
                                        .getCourseId()
                        )

                        .courseTitle(
                                enrollment
                                        .getCourse()
                                        .getTitle()
                        )

                        .courseImageUrl(
                                enrollment
                                        .getCourse()
                                        .getImageUrl()
                        )

                        .coursePrice(
                                enrollment
                                        .getCourse()
                                        .getPrice()
                        )

                        .duration(
                                enrollment
                                        .getCourse()
                                        .getDuration()
                        )


                        // ====================================================
                        // COURSE SCHEDULE
                        // ====================================================

                        .startDate(
                                enrollment
                                        .getCourse()
                                        .getStartDate()
                        )

                        .startTime(
                                enrollment
                                        .getCourse()
                                        .getStartTime()
                        )

                        .endTime(
                                enrollment
                                        .getCourse()
                                        .getEndTime()
                        )

                        .classDays(
                                enrollment
                                        .getCourse()
                                        .getClassDays()
                        )


                        // ====================================================
                        // ORDER
                        // ====================================================

                        .orderId(
                                enrollment
                                        .getOrder()
                                        .getOrderId()
                        )

                        .orderNumber(
                                enrollment
                                        .getOrder()
                                        .getOrderNumber()
                        )


                        // ====================================================
                        // ENROLLMENT STATUS
                        // ====================================================

                        .status(
                                enrollment.getStatus()
                        )


                        // ====================================================
                        // DATES
                        // ====================================================

                        .enrolledAt(
                                enrollment.getEnrolledAt()
                        )

                        .completedAt(
                                enrollment.getCompletedAt()
                        )

                        .expiresAt(
                                enrollment.getExpiresAt()
                        )


                        // ====================================================
                        // TIMESTAMP
                        // ====================================================

                        .updatedAt(
                                enrollment.getUpdatedAt()
                        )


                        .build();
        }
}