package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private Long profileId;

    private Long userId;

    private String fullName;

    private String email;

    private LocalDate dateOfBirth;

    private LocalTime timeOfBirth;

    private String placeOfBirth;

    private String gender;

    private String address;

    private String city;

    private String state;

    private String country;

    private String pincode;

    private String bio;

    private String profileImage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}