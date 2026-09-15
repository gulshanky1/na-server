package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.StudentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Student;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import org.springframework.stereotype.Component;

@Component
public class StudentTransformer {

    public StudentResponse toResponse(Student student) {

        User user = student.getUser();

        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .studentCode(student.getStudentCode())
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profileImage(user.getProfileImage())
                .active(student.isActive())
                .registeredAt(student.getRegisteredAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}