package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private Long studentId;

    private String studentCode;

    private Long userId;

    private String fullName;

    private String email;

    private String phone;

    private String profileImage;

    private boolean active;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;
}