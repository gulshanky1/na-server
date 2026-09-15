package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.StudentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Student;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.StudentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.StudentTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentTransformer studentTransformer;

    @Transactional(readOnly = true)
    public StudentResponse getMyStudent(Long userId) {

        Student student =
                studentRepository
                        .findByUser_UserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student record not found"
                                )
                        );

        return studentTransformer.toResponse(student);
    }

    @Transactional
    public Student createIfNotExists(Long userId) {

        return studentRepository
                .findByUser_UserId(userId)
                .orElseGet(() -> {

                    User user =
                            userRepository.findById(userId)
                                    .orElseThrow(() ->
                                            new ResourceNotFoundException(
                                                    "User not found"
                                            )
                                    );

                    Student student = Student.builder()
                            .user(user)
                            .studentCode(generateStudentCode())
                            .active(true)
                            .build();

                    return studentRepository.save(student);
                });
    }

    private String generateStudentCode() {

        long nextId =
                studentRepository.count() + 1;

        return String.format(
                "STU-%06d",
                nextId
        );
    }
}