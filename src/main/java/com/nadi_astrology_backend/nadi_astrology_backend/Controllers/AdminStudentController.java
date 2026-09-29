package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.StudentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;

    // =========================================================
    // GET ALL STUDENTS - ADMIN
    // =========================================================

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getAllStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "registeredAt"
                )
        );

        Page<StudentResponse> students =
                studentService.getAllStudents(
                        search,
                        active,
                        pageable
                );

        ApiResponse<Page<StudentResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Students fetched successfully",
                        students
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET STUDENT BY ID - ADMIN
    // =========================================================

    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudent(
            @PathVariable Long studentId
    ) {

        StudentResponse student =
                studentService.getStudentById(studentId);

        ApiResponse<StudentResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Student fetched successfully",
                        student
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE STUDENT STATUS - ADMIN
    // =========================================================

    @PatchMapping("/{studentId}/status")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentStatus(
            @PathVariable Long studentId,
            @RequestParam boolean active
    ) {

        StudentResponse student =
                studentService.updateStudentStatus(
                        studentId,
                        active
                );

        ApiResponse<StudentResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        active
                                ? "Student activated successfully"
                                : "Student deactivated successfully",
                        student
                );

        return ResponseEntity.ok(response);
    }
}