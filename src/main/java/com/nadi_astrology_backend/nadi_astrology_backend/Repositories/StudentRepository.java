package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long> {

    Optional<Student> findByUser_UserId(Long userId);

    boolean existsByUser_UserId(Long userId);

    Optional<Student> findByStudentCode(String studentCode);
}