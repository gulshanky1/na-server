package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long> {

    Optional<Student> findByUser_UserId(Long userId);

    boolean existsByUser_UserId(Long userId);

    Optional<Student> findByStudentCode(String studentCode);

    // =========================================================
    // ADMIN STUDENT SEARCH
    // =========================================================

    @Query("""
            SELECT s
            FROM Student s
            JOIN s.user u
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(s.studentCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.fullName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.email)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.phone)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
            AND (
                :active IS NULL
                OR s.active = :active
            )
            """)
    Page<Student> searchStudents(
            @Param("search") String search,
            @Param("active") Boolean active,
            Pageable pageable
    );
}