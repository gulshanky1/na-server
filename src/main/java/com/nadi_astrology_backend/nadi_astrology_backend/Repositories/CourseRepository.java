package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository
        extends JpaRepository<Course, Long> {

    // Get only active courses
    Page<Course> findByActiveTrue(
            Pageable pageable
    );

    // Check whether a course title already exists
    boolean existsByTitleIgnoreCase(
            String title
    );

    // Find course by title
    Optional<Course> findByTitleIgnoreCase(
            String title
    );
}