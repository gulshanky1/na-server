package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.LiveClassStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "live_classes",
        indexes = {
                @Index(
                        name = "idx_live_class_course_id",
                        columnList = "course_id"
                ),
                @Index(
                        name = "idx_live_class_date",
                        columnList = "class_date"
                ),
                @Index(
                        name = "idx_live_class_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_live_class_course_date",
                        columnList = "course_id, class_date"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiveClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long liveClassId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_id",
            nullable = false
    )
    private Course course;

    @Column(
            nullable = false,
            length = 200
    )
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "zoom_meeting_id")
    private Long zoomMeetingId;

    @Column(
            name = "zoom_join_url",
            length = 1000
    )
    private String zoomJoinUrl;

    @Column(
            name = "zoom_start_url",
            length = 1000
    )
    private String zoomStartUrl;

    @Column(
            name = "zoom_password",
            length = 100
    )
    private String zoomPassword;

    @Column(
            name = "class_date",
            nullable = false
    )
    private LocalDate classDate;

    @Column(
            name = "start_time",
            nullable = false
    )
    private LocalTime startTime;

    @Column(
            name = "end_time",
            nullable = false
    )
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    @Builder.Default
    private LiveClassStatus status =
            LiveClassStatus.SCHEDULED;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}