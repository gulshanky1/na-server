package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.LiveClassStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.LiveClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LiveClassRepository extends JpaRepository<LiveClass, Long> {

    Page<LiveClass> findByCourse_CourseId(
            Long courseId,
            Pageable pageable
    );

    Page<LiveClass> findByStatus(
            LiveClassStatus status,
            Pageable pageable
    );

    List<LiveClass> findByCourse_CourseIdAndStatus(
            Long courseId,
            LiveClassStatus status
    );

    List<LiveClass> findByClassDateGreaterThanEqualOrderByClassDateAscStartTimeAsc(
            LocalDate date
    );

    List<LiveClass> findByCourse_CourseIdAndClassDateGreaterThanEqualOrderByClassDateAscStartTimeAsc(
            Long courseId,
            LocalDate date
    );
}