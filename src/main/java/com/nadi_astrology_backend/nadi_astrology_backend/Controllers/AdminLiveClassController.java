package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.LiveClassRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.LiveClassResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.LiveClassService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/live-classes")
@RequiredArgsConstructor
@Validated
public class AdminLiveClassController {

    private final LiveClassService liveClassService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public ResponseEntity<LiveClassResponse> createLiveClass(
            @Valid @RequestBody LiveClassRequest request
    ) {

        return ResponseEntity.ok(
                liveClassService.createLiveClass(request)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ResponseEntity<Page<LiveClassResponse>> getAllLiveClasses(

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size cannot exceed 50")
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.ASC,
                                "classDate"
                        ).and(
                                Sort.by(
                                        Sort.Direction.ASC,
                                        "startTime"
                                )
                        )
                );

        return ResponseEntity.ok(
                liveClassService.getAllLiveClasses(pageable)
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{liveClassId}")
    public ResponseEntity<LiveClassResponse> getLiveClass(

            @PathVariable
            @Positive(message = "Live class ID must be greater than 0")
            Long liveClassId
    ) {

        return ResponseEntity.ok(
                liveClassService.getLiveClass(liveClassId)
        );
    }

    // =========================
    // GET BY COURSE
    // =========================

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<LiveClassResponse>> getLiveClassesByCourse(

            @PathVariable
            @Positive(message = "Course ID must be greater than 0")
            Long courseId,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size cannot exceed 50")
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.ASC,
                                "classDate"
                        ).and(
                                Sort.by(
                                        Sort.Direction.ASC,
                                        "startTime"
                                )
                        )
                );

        return ResponseEntity.ok(
                liveClassService.getLiveClassesByCourse(
                        courseId,
                        pageable
                )
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{liveClassId}")
    public ResponseEntity<LiveClassResponse> updateLiveClass(

            @PathVariable
            @Positive(message = "Live class ID must be greater than 0")
            Long liveClassId,

            @Valid @RequestBody LiveClassRequest request
    ) {

        return ResponseEntity.ok(
                liveClassService.updateLiveClass(
                        liveClassId,
                        request
                )
        );
    }

    // =========================
    // START CLASS
    // =========================

    @PostMapping("/{liveClassId}/start")
    public ResponseEntity<LiveClassResponse> startLiveClass(

            @PathVariable
            @Positive(message = "Live class ID must be greater than 0")
            Long liveClassId
    ) {

        return ResponseEntity.ok(
                liveClassService.startLiveClass(liveClassId)
        );
    }

    // =========================
    // DELETE
    // =========================

    @DeleteMapping("/{liveClassId}")
    public ResponseEntity<Void> deleteLiveClass(

            @PathVariable
            @Positive(message = "Live class ID must be greater than 0")
            Long liveClassId
    ) {

        liveClassService.deleteLiveClass(liveClassId);

        return ResponseEntity.noContent().build();
    }

    // =========================
    // UPCOMING
    // =========================

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingLiveClasses() {

        return ResponseEntity.ok(
                liveClassService.getUpcomingLiveClasses()
        );
    }
}