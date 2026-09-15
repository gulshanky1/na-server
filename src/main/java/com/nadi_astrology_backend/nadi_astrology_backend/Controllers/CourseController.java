package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CourseResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // =========================================================
    // GET ACTIVE COURSES - PUBLIC
    // =========================================================

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getActiveCourses(
            Pageable pageable
    ) {

        Page<CourseResponse> courses =
                courseService.getActiveCourses(pageable);

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
    // GET COURSE BY ID - PUBLIC
    // =========================================================

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(
            @PathVariable Long courseId
    ) {

        CourseResponse course =
                courseService.getCourseById(courseId);

        ApiResponse<CourseResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Course fetched successfully",
                        course
                );

        return ResponseEntity.ok(response);
    }
}