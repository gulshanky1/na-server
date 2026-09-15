package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.LiveClassResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.StudentLiveClassResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.LiveClass;
import org.springframework.stereotype.Component;

@Component
public class LiveClassTransformer {

    // =====================================================
    // ADMIN RESPONSE
    // =====================================================

    public LiveClassResponse toResponse(
            LiveClass liveClass
    ) {

        return LiveClassResponse.builder()

                .liveClassId(
                        liveClass.getLiveClassId()
                )

                .courseId(
                        liveClass.getCourse().getCourseId()
                )

                .courseTitle(
                        liveClass.getCourse().getTitle()
                )

                .title(
                        liveClass.getTitle()
                )

                .description(
                        liveClass.getDescription()
                )

                .classDate(
                        liveClass.getClassDate()
                )

                .startTime(
                        liveClass.getStartTime()
                )

                .endTime(
                        liveClass.getEndTime()
                )

                .status(
                        liveClass.getStatus()
                )

                // Full Zoom information for ADMIN
                .zoomMeetingId(
                        liveClass.getZoomMeetingId()
                )

                .zoomJoinUrl(
                        liveClass.getZoomJoinUrl()
                )

                .zoomStartUrl(
                        liveClass.getZoomStartUrl()
                )

                .zoomPassword(
                        liveClass.getZoomPassword()
                )

                .createdAt(
                        liveClass.getCreatedAt()
                )

                .updatedAt(
                        liveClass.getUpdatedAt()
                )

                .build();
    }


    // =====================================================
    // STUDENT RESPONSE
    // =====================================================

    public StudentLiveClassResponse toStudentResponse(
            LiveClass liveClass
    ) {

        return StudentLiveClassResponse.builder()

                .liveClassId(
                        liveClass.getLiveClassId()
                )

                .courseId(
                        liveClass.getCourse().getCourseId()
                )

                .courseTitle(
                        liveClass.getCourse().getTitle()
                )

                .title(
                        liveClass.getTitle()
                )

                .description(
                        liveClass.getDescription()
                )

                .classDate(
                        liveClass.getClassDate()
                )

                .startTime(
                        liveClass.getStartTime()
                )

                .endTime(
                        liveClass.getEndTime()
                )

                .status(
                        liveClass.getStatus()
                )

                // ONLY Zoom Join URL
                .zoomJoinUrl(
                        liveClass.getZoomJoinUrl()
                )

                .createdAt(
                        liveClass.getCreatedAt()
                )

                .updatedAt(
                        liveClass.getUpdatedAt()
                )

                .build();
    }
}