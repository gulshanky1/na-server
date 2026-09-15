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
public class LiveClassResponse {

    private Long liveClassId;

    private Long courseId;
    private String courseTitle;

    private String title;
    private String description;

    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private LiveClassStatus status;

    // Zoom details
    private Long zoomMeetingId;
    private String zoomJoinUrl;
    private String zoomStartUrl;
    private String zoomPassword;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}