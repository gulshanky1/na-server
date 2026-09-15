package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.LiveClassStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentLiveClassResponse {

    private Long liveClassId;

    private Long courseId;
    private String courseTitle;

    private String title;
    private String description;

    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private LiveClassStatus status;

    // Student gets ONLY Zoom Join URL
    private String zoomJoinUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}