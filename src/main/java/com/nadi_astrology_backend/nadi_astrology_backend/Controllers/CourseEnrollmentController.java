package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CourseEnrollmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.CourseEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class CourseEnrollmentController {

    private final CourseEnrollmentService enrollmentService;


    // ============================================================
    // MY COURSES
    // ============================================================

    @GetMapping("/me")
    public ResponseEntity<List<CourseEnrollmentResponse>>
    getMyEnrollments(
            Authentication authentication
    ) {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser)
                        authentication.getPrincipal();


        Long userId =
                authenticatedUser.getUserId();


        return ResponseEntity.ok(
                enrollmentService.getMyEnrollments(
                        userId
                )
        );
    }


    // ============================================================
    // MY ACTIVE COURSES
    // ============================================================

    @GetMapping("/me/active")
    public ResponseEntity<List<CourseEnrollmentResponse>>
    getMyActiveEnrollments(
            Authentication authentication
    ) {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser)
                        authentication.getPrincipal();


        Long userId =
                authenticatedUser.getUserId();


        return ResponseEntity.ok(
                enrollmentService.getMyActiveEnrollments(
                        userId
                )
        );
    }
}