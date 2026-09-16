package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Config.ZoomConfig;
import com.nadi_astrology_backend.nadi_astrology_backend.DTO.LiveClassRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.LiveClassResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.StudentLiveClassResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.EnrollmentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.LiveClassStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Course;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.CourseEnrollment;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.LiveClass;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.CourseEnrollmentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.CourseRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.LiveClassRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.LiveClassTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LiveClassService {

    private final LiveClassRepository liveClassRepository;
    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final LiveClassTransformer liveClassTransformer;

    private final ZoomConfig zoomConfig;
    private final ZoomMeetingService zoomMeetingService;

    private final EmailService emailService;
    private final NotificationService notificationService;



    // =====================================================
    // ADMIN - CREATE LIVE CLASS
    // =====================================================

    @Transactional
    public LiveClassResponse createLiveClass(
            LiveClassRequest request
    ) {


        // -------------------------------------------------
        // 1. Validate time
        // -------------------------------------------------

        validateTime(request);


        // -------------------------------------------------
        // 2. Find course
        // -------------------------------------------------

        Course course =
                courseRepository.findById(
                        request.getCourseId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id: "
                                        + request.getCourseId()
                        )
                );


        // -------------------------------------------------
        // 3. Check course active
        // -------------------------------------------------

        if (!course.isActive()) {

            throw new BadRequestException(
                    "Cannot create live class for an inactive course"
            );
        }


        // -------------------------------------------------
        // 4. Calculate Zoom duration
        // -------------------------------------------------

        long durationMinutes =
                Duration.between(
                        request.getStartTime(),
                        request.getEndTime()
                ).toMinutes();


        // -------------------------------------------------
        // 5. Create Zoom meeting
        // -------------------------------------------------

        Map<String, Object> zoomMeeting =
                zoomMeetingService.createMeeting(

                        zoomConfig.getHostUserId(),

                        request.getTitle(),

                        request.getDescription(),

                        request.getClassDate()
                                .atTime(
                                        request.getStartTime()
                                )
                                .toString(),

                        (int) durationMinutes
                );


        // -------------------------------------------------
        // 6. Create LiveClass entity
        // -------------------------------------------------

        LiveClass liveClass =
                LiveClass.builder()

                        .course(course)

                        .title(
                                request.getTitle()
                        )

                        .description(
                                request.getDescription()
                        )

                        .classDate(
                                request.getClassDate()
                        )

                        .startTime(
                                request.getStartTime()
                        )

                        .endTime(
                                request.getEndTime()
                        )

                        .status(
                                request.getStatus() != null
                                        ? request.getStatus()
                                        : LiveClassStatus.SCHEDULED
                        )

                        // ---------------------------------
                        // Zoom details
                        // ---------------------------------

                        .zoomMeetingId(
                                Long.valueOf(
                                        zoomMeeting
                                                .get("id")
                                                .toString()
                                )
                        )

                        .zoomJoinUrl(
                                zoomMeeting
                                        .get("join_url")
                                        .toString()
                        )

                        .zoomStartUrl(
                                zoomMeeting
                                        .get("start_url")
                                        .toString()
                        )

                        .zoomPassword(
                                zoomMeeting.get("password") != null
                                        ? zoomMeeting
                                        .get("password")
                                        .toString()
                                        : null
                        )

                        .build();


        // -------------------------------------------------
        // 7. Save LiveClass
        // -------------------------------------------------

        LiveClass savedLiveClass =
                liveClassRepository.save(
                        liveClass
                );


        // -------------------------------------------------
        // 8. Return admin response
        // -------------------------------------------------

        return liveClassTransformer.toResponse(
                savedLiveClass
        );
    }


    // =====================================================
    // ADMIN - UPDATE LIVE CLASS
    // =====================================================

    @Transactional
    public LiveClassResponse updateLiveClass(
            Long liveClassId,
            LiveClassRequest request
    ) {

        // -------------------------------------------------
        // 1. Validate time
        // -------------------------------------------------

        validateTime(request);


        // -------------------------------------------------
        // 2. Find existing LiveClass
        // -------------------------------------------------

        LiveClass liveClass =
                liveClassRepository.findById(
                        liveClassId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Live class not found with id: "
                                        + liveClassId
                        )
                );


        // -------------------------------------------------
        // 3. Find course
        // -------------------------------------------------

        Course course =
                courseRepository.findById(
                        request.getCourseId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id: "
                                        + request.getCourseId()
                        )
                );


        // -------------------------------------------------
        // 4. Check course active
        // -------------------------------------------------

        if (!course.isActive()) {

            throw new BadRequestException(
                    "Cannot assign live class to an inactive course"
            );
        }


        // -------------------------------------------------
        // 5. Calculate new Zoom duration
        // -------------------------------------------------

        long durationMinutes =
                Duration.between(
                        request.getStartTime(),
                        request.getEndTime()
                ).toMinutes();


        // -------------------------------------------------
        // 6. Update Zoom meeting
        // -------------------------------------------------

        if (liveClass.getZoomMeetingId() != null) {

            zoomMeetingService.updateMeeting(

                    liveClass.getZoomMeetingId(),

                    request.getTitle(),

                    request.getDescription(),

                    request.getClassDate()
                            .atTime(
                                    request.getStartTime()
                            )
                            .toString(),

                    (int) durationMinutes
            );
        }


        // -------------------------------------------------
        // 7. Update LiveClass entity
        // -------------------------------------------------

        liveClass.setCourse(
                course
        );

        liveClass.setTitle(
                request.getTitle()
        );

        liveClass.setDescription(
                request.getDescription()
        );

        liveClass.setClassDate(
                request.getClassDate()
        );

        liveClass.setStartTime(
                request.getStartTime()
        );

        liveClass.setEndTime(
                request.getEndTime()
        );


        // -------------------------------------------------
        // 8. Update status
        // -------------------------------------------------

        if (request.getStatus() != null) {

            liveClass.setStatus(
                    request.getStatus()
            );
        }


        // -------------------------------------------------
        // 9. Save updated LiveClass
        // -------------------------------------------------

        LiveClass updatedLiveClass =
                liveClassRepository.save(
                        liveClass
                );


        // -------------------------------------------------
        // 10. Return response
        // -------------------------------------------------

        return liveClassTransformer.toResponse(
                updatedLiveClass
        );
    }


    // =====================================================
    // ADMIN - DELETE LIVE CLASS
    // =====================================================

    @Transactional
    public void deleteLiveClass(
            Long liveClassId
    ) {

        // -------------------------------------------------
        // 1. Find LiveClass
        // -------------------------------------------------

        LiveClass liveClass =
                liveClassRepository.findById(
                        liveClassId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Live class not found with id: "
                                        + liveClassId
                        )
                );


        // -------------------------------------------------
        // 2. Delete Zoom meeting
        // -------------------------------------------------

        if (liveClass.getZoomMeetingId() != null) {

            zoomMeetingService.deleteMeeting(
                    liveClass.getZoomMeetingId()
            );
        }


        // -------------------------------------------------
        // 3. Delete LiveClass from database
        // -------------------------------------------------

        liveClassRepository.delete(
                liveClass
        );
    }


    // =====================================================
    // ADMIN - GET LIVE CLASS BY ID
    // =====================================================

    @Transactional(readOnly = true)
    public LiveClassResponse getLiveClass(
            Long liveClassId
    ) {

        LiveClass liveClass =
                liveClassRepository.findById(
                        liveClassId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Live class not found with id: "
                                        + liveClassId
                        )
                );

        return liveClassTransformer.toResponse(
                liveClass
        );
    }


    // =====================================================
    // ADMIN - GET ALL LIVE CLASSES
    // =====================================================

    @Transactional(readOnly = true)
    public Page<LiveClassResponse> getAllLiveClasses(
            Pageable pageable
    ) {

        return liveClassRepository
                .findAll(pageable)
                .map(
                        liveClassTransformer::toResponse
                );
    }


    // =====================================================
    // ADMIN - GET LIVE CLASSES BY COURSE
    // =====================================================

    @Transactional(readOnly = true)
    public Page<LiveClassResponse> getLiveClassesByCourse(
            Long courseId,
            Pageable pageable
    ) {

        if (!courseRepository.existsById(courseId)) {

            throw new ResourceNotFoundException(
                    "Course not found with id: "
                            + courseId
            );
        }

        return liveClassRepository
                .findByCourse_CourseId(
                        courseId,
                        pageable
                )
                .map(
                        liveClassTransformer::toResponse
                );
    }


    // =====================================================
    // ADMIN - GET UPCOMING LIVE CLASSES
    // =====================================================

    @Transactional(readOnly = true)
    public List<LiveClassResponse> getUpcomingLiveClasses() {

        return liveClassRepository
                .findByClassDateGreaterThanEqualOrderByClassDateAscStartTimeAsc(
                        LocalDate.now()
                )
                .stream()
                .map(
                        liveClassTransformer::toResponse
                )
                .toList();
    }


    // =====================================================
    // STUDENT - MY LIVE CLASSES
    // =====================================================

    @Transactional(readOnly = true)
    public List<StudentLiveClassResponse> getMyLiveClasses(
            Long userId
    ) {

        // -------------------------------------------------
        // 1. Find active enrollments
        // -------------------------------------------------

        List<CourseEnrollment> enrollments =
                courseEnrollmentRepository
                        .findActiveEnrollmentsByUserId(
                                userId,
                                EnrollmentStatus.ACTIVE
                        );


        // -------------------------------------------------
        // 2. Find upcoming classes
        // -------------------------------------------------

        return enrollments.stream()

                .flatMap(enrollment ->
                        liveClassRepository
                                .findByCourse_CourseIdAndClassDateGreaterThanEqualOrderByClassDateAscStartTimeAsc(
                                        enrollment
                                                .getCourse()
                                                .getCourseId(),
                                        LocalDate.now()
                                )
                                .stream()
                )


                // -------------------------------------------------
                // 3. Convert to student response
                // -------------------------------------------------

                .map(
                        liveClassTransformer::toStudentResponse
                )

                .toList();
    }

    @Transactional
    public LiveClassResponse startLiveClass(Long liveClassId) {

        LiveClass liveClass = liveClassRepository.findById(liveClassId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Live class not found with id: " + liveClassId
                        )
                );

        if (liveClass.getStatus() == LiveClassStatus.LIVE) {
            throw new BadRequestException("Live class is already started");
        }

        liveClass.setStatus(LiveClassStatus.LIVE);

        LiveClass savedLiveClass = liveClassRepository.save(liveClass);

        List<CourseEnrollment> enrollments =
                courseEnrollmentRepository.findActiveEnrollmentsByCourseId(
                        liveClass.getCourse().getCourseId(),
                        EnrollmentStatus.ACTIVE
                );

        for (CourseEnrollment enrollment : enrollments) {

            Long userId = enrollment.getStudent()
                    .getUser()
                    .getUserId();

            String email = enrollment.getStudent()
                    .getUser()
                    .getEmail();

            String title = "Your live class is starting";

            String message =
                    "Your live class \"" + liveClass.getTitle() +
                            "\" is now live. Join the class using the Zoom link.";

            notificationService.createNotification(
                    userId,
                    com.nadi_astrology_backend.nadi_astrology_backend.Enum.NotificationType.LIVE_CLASS,
                    title,
                    message,
                    liveClass.getLiveClassId(),
                    "LIVE_CLASS"
            );

            String htmlContent = """
                <html>
                <body>
                    <h2>Your Live Class Is Starting</h2>

                    <p>Hello,</p>

                    <p>
                        Your live class <strong>%s</strong> is now starting.
                    </p>

                    <p>
                        <strong>Date:</strong> %s<br>
                        <strong>Time:</strong> %s - %s
                    </p>

                    <p>
                        <a href="%s">
                            Join Zoom Class
                        </a>
                    </p>

                    <p>
                        <strong>Zoom Password:</strong> %s
                    </p>

                    <p>
                        Please join the class on time.
                    </p>
                </body>
                </html>
                """.formatted(
                    liveClass.getTitle(),
                    liveClass.getClassDate(),
                    liveClass.getStartTime(),
                    liveClass.getEndTime(),
                    liveClass.getZoomJoinUrl(),
                    liveClass.getZoomPassword() != null
                            ? liveClass.getZoomPassword()
                            : "No password required"
            );

            emailService.sendHtmlEmail(
                    email,
                    "Your Live Class Is Starting - " + liveClass.getTitle(),
                    htmlContent
            );
        }

        return liveClassTransformer.toResponse(savedLiveClass);
    }

    // =====================================================
    // VALIDATE TIME
    // =====================================================

    private void validateTime(
            LiveClassRequest request
    ) {

        if (
                request.getStartTime() != null
                        &&
                        request.getEndTime() != null
                        &&
                        !request
                                .getEndTime()
                                .isAfter(
                                        request.getStartTime()
                                )
        ) {

            throw new BadRequestException(
                    "End time must be after start time"
            );
        }
    }
}