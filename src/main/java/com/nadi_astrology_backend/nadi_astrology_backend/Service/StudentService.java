package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.StudentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Student;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.StudentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.StudentTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<StudentResponse> getAllStudents(
            String search,
            Boolean active,
            Pageable pageable
    ) {

        return studentRepository
                .searchStudents(search, active, pageable)
                .map(studentTransformer::toResponse);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long studentId) {

        Student student =
                studentRepository.findById(studentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found"
                                )
                        );

        return studentTransformer.toResponse(student);
    }

    @Transactional
    public StudentResponse updateStudentStatus(
            Long studentId,
            boolean active
    ) {

        Student student =
                studentRepository.findById(studentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found"
                                )
                        );

        student.setActive(active);

        Student savedStudent =
                studentRepository.save(student);

        return studentTransformer.toResponse(savedStudent);
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
                            .active(true)
                            .build();

                    Student savedStudent =
                            studentRepository.saveAndFlush(student);

                    savedStudent.setStudentCode(
                            String.format(
                                    "STU-%06d",
                                    savedStudent.getStudentId()
                            )
                    );

                    return studentRepository.save(
                            savedStudent
                    );
                });
    }
}