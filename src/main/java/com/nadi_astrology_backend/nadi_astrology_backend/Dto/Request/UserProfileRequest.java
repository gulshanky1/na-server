package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRequest {

    private LocalDate dateOfBirth;

    private LocalTime timeOfBirth;

    @Size(max = 150, message = "Place of birth cannot exceed 150 characters")
    private String placeOfBirth;

    @Size(max = 20, message = "Gender cannot exceed 20 characters")
    private String gender;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @Size(max = 10, message = "Pincode cannot exceed 10 characters")
    private String pincode;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    @Size(max = 500, message = "Profile image URL cannot exceed 500 characters")
    private String profileImage;
}