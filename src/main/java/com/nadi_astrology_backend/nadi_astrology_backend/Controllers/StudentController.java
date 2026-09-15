package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.StudentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/me")
    public ResponseEntity<StudentResponse> getMyStudent(
            Authentication authentication
    ) {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) authentication.getPrincipal();

        Long userId =
                authenticatedUser.getUserId();

        return ResponseEntity.ok(
                studentService.getMyStudent(userId)
        );
    }
}