package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CourseRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CourseResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseTransformer {


    // ============================================================
    // REQUEST -> ENTITY
    // ============================================================

    public Course toEntity(
            CourseRequest request
    ) {

        return Course.builder()

                .title(
                        request.getTitle().trim()
                )

                .shortDescription(
                        request.getShortDescription()
                )

                .description(
                        request.getDescription().trim()
                )

                .imageUrl(
                        request.getImageUrl()
                )

                .duration(
                        request.getDuration().trim()
                )

                .syllabus(
                        request.getSyllabus()
                )

                .price(
                        request.getPrice()
                )

                .startDate(
                        request.getStartDate()
                )

                .startTime(
                        request.getStartTime()
                )

                .endTime(
                        request.getEndTime()
                )

                .classDays(
                        request.getClassDays().trim()
                )

                .active(
                        request.getActive() == null
                                || request.getActive()
                )

                .tags(
                        request.getTags()
                )

                .build();
    }


    // ============================================================
    // UPDATE ENTITY
    // ============================================================

    public void updateEntity(
            Course course,
            CourseRequest request
    ) {

        course.setTitle(
                request.getTitle().trim()
        );

        course.setShortDescription(
                request.getShortDescription()
        );

        course.setDescription(
                request.getDescription().trim()
        );

        course.setImageUrl(
                request.getImageUrl()
        );

        course.setDuration(
                request.getDuration().trim()
        );

        course.setSyllabus(
                request.getSyllabus()
        );

        course.setPrice(
                request.getPrice()
        );

        course.setStartDate(
                request.getStartDate()
        );

        course.setStartTime(
                request.getStartTime()
        );

        course.setEndTime(
                request.getEndTime()
        );

        course.setClassDays(
                request.getClassDays().trim()
        );

        course.setTags(
                request.getTags()
        );


        /*
         * Only update active when supplied.
         */
        if (request.getActive() != null) {

            course.setActive(
                    request.getActive()
            );
        }
    }


    // ============================================================
    // ENTITY -> RESPONSE
    // ============================================================

    public CourseResponse toResponse(
            Course course
    ) {

        return CourseResponse.builder()

                .courseId(
                        course.getCourseId()
                )

                .title(
                        course.getTitle()
                )

                .shortDescription(
                        course.getShortDescription()
                )

                .description(
                        course.getDescription()
                )

                .imageUrl(
                        course.getImageUrl()
                )

                .duration(
                        course.getDuration()
                )

                .syllabus(
                        course.getSyllabus()
                )

                .price(
                        course.getPrice()
                )

                .startDate(
                        course.getStartDate()
                )

                .startTime(
                        course.getStartTime()
                )

                .endTime(
                        course.getEndTime()
                )

                .classDays(
                        course.getClassDays()
                )

                .active(
                        course.isActive()
                )

                .tags(
                        course.getTags()
                )

                .createdAt(
                        course.getCreatedAt()
                )

                .updatedAt(
                        course.getUpdatedAt()
                )

                .build();
    }
}