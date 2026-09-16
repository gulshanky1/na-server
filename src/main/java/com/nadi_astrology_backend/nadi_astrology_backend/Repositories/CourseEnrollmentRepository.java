package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.EnrollmentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.CourseEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseEnrollmentRepository
        extends JpaRepository<CourseEnrollment, Long> {


    // ============================================================
    // CHECK ENROLLMENT
    // ============================================================

    boolean existsByStudent_StudentIdAndCourse_CourseId(
            Long studentId,
            Long courseId
    );


    // ============================================================
    // FIND SPECIFIC ENROLLMENT
    // ============================================================

    Optional<CourseEnrollment>
    findByStudent_StudentIdAndCourse_CourseId(
            Long studentId,
            Long courseId
    );


    // ============================================================
    // STUDENT ENROLLMENTS
    // ============================================================

    @Query("""
            SELECT e
            FROM CourseEnrollment e
            JOIN FETCH e.course
            WHERE e.student.studentId = :studentId
            ORDER BY e.enrolledAt DESC
            """)
    List<CourseEnrollment> findStudentEnrollments(
            @Param("studentId") Long studentId
    );


    // ============================================================
    // STUDENT ENROLLMENTS BY STATUS
    // ============================================================

    @Query("""
            SELECT e
            FROM CourseEnrollment e
            JOIN FETCH e.course
            WHERE e.student.studentId = :studentId
            AND e.status = :status
            ORDER BY e.enrolledAt DESC
            """)
    List<CourseEnrollment> findStudentEnrollmentsByStatus(
            @Param("studentId") Long studentId,
            @Param("status") EnrollmentStatus status
    );


    // ============================================================
    // STUDENT ENROLLMENTS - PAGINATED
    // ============================================================

    Page<CourseEnrollment> findByStudent_StudentId(
            Long studentId,
            Pageable pageable
    );


    // ============================================================
    // STUDENT ENROLLMENTS BY STATUS - PAGINATED
    // ============================================================

    Page<CourseEnrollment> findByStudent_StudentIdAndStatus(
            Long studentId,
            EnrollmentStatus status,
            Pageable pageable
    );


    // ============================================================
    // COUNT COURSE STUDENTS
    // ============================================================

    long countByCourse_CourseIdAndStatus(
            Long courseId,
            EnrollmentStatus status
    );

    // ============================================================
    // STUDENT - ACTIVE ENROLLMENTS BY USER ID
    // ============================================================

    @Query("""
            SELECT e
            FROM CourseEnrollment e
            JOIN FETCH e.course
            WHERE e.student.user.userId = :userId
            AND e.status = :status
            """)
    List<CourseEnrollment> findActiveEnrollmentsByUserId(
            @Param("userId") Long userId,
            @Param("status") EnrollmentStatus status
    );


    // ============================================================
// ACTIVE STUDENTS BY COURSE
// ============================================================

    @Query("""
        SELECT e
        FROM CourseEnrollment e
        JOIN FETCH e.student s
        JOIN FETCH s.user u
        WHERE e.course.courseId = :courseId
        AND e.status = :status
        """)
    List<CourseEnrollment> findActiveEnrollmentsByCourseId(
            @Param("courseId") Long courseId,
            @Param("status") EnrollmentStatus status
    );

}