package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CourseRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CourseResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/courses")
@RequiredArgsConstructor
@Validated
public class AdminCourseController {

    private final CourseService courseService;

    // =========================================================
    // GET ALL COURSES - ADMIN
    // =========================================================

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllCourses(

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size cannot exceed 50")
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<CourseResponse> courses =
                courseService.getAllCourses(pageable);

        ApiResponse<Page<CourseResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Courses fetched successfully",
                        courses
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // CREATE COURSE - ADMIN
    // =========================================================

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseRequest request
    ) {

        CourseResponse course =
                courseService.createCourse(request);

        ApiResponse<CourseResponse> response =
                new ApiResponse<>(
                        false,
                        201,
                        "Course created successfully",
                        course
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // UPDATE COURSE - ADMIN
    // =========================================================

    @PutMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(

            @PathVariable
            @Positive(message = "Course ID must be greater than 0")
            Long courseId,

            @Valid @RequestBody CourseRequest request
    ) {

        CourseResponse course =
                courseService.updateCourse(
                        courseId,
                        request
                );

        ApiResponse<CourseResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Course updated successfully",
                        course
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DEACTIVATE COURSE - ADMIN
    // =========================================================

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseResponse>> deactivateCourse(

            @PathVariable
            @Positive(message = "Course ID must be greater than 0")
            Long courseId
    ) {

        CourseResponse course =
                courseService.deactivateCourse(courseId);

        ApiResponse<CourseResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Course deactivated successfully",
                        course
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPLOAD COURSE IMAGE - ADMIN
    // =========================================================

    @PostMapping(
            value = "/{courseId}/image",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ApiResponse<CourseResponse>> uploadCourseImage(

            @PathVariable
            @Positive(message = "Course ID must be greater than 0")
            Long courseId,

            @RequestParam("file")
            @NotNull(message = "Image file is required")
            MultipartFile file
    ) {

        CourseResponse course =
                courseService.uploadCourseImage(
                        courseId,
                        file
                );

        ApiResponse<CourseResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Course image uploaded successfully",
                        course
                );

        return ResponseEntity.ok(response);
    }
}