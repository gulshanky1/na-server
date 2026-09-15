package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CourseRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CourseResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    // =========================================================
    // GET ALL COURSES - ADMIN
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getAllCourses(
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                courseService.getAllCourses(pageable)
        );
    }

    // =========================================================
    // CREATE COURSE - ADMIN
    // =========================================================

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseRequest request
    ) {
        return ResponseEntity.ok(
                courseService.createCourse(request)
        );
    }

    // =========================================================
    // UPDATE COURSE - ADMIN
    // =========================================================

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseRequest request
    ) {
        return ResponseEntity.ok(
                courseService.updateCourse(courseId, request)
        );
    }

    // =========================================================
    // DEACTIVATE COURSE - ADMIN
    // =========================================================

    @DeleteMapping("/{courseId}")
    public ResponseEntity<CourseResponse> deactivateCourse(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                courseService.deactivateCourse(courseId)
        );
    }

    // =========================================================
    // UPLOAD COURSE IMAGE - ADMIN
    // =========================================================

    @PostMapping(
            value = "/{courseId}/image",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<CourseResponse> uploadCourseImage(
            @PathVariable Long courseId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                courseService.uploadCourseImage(courseId, file)
        );
    }
}