package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInformationRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 150, message = "Full name cannot exceed 150 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(max = 20, message = "Phone cannot exceed 20 characters")
    private String phone;


    // ============================================================
    // ASTROLOGY INFORMATION
    // ============================================================

    private LocalDate dateOfBirth;

    private LocalTime birthTime;

    @Size(max = 150, message = "Birth place cannot exceed 150 characters")
    private String birthPlace;

    @Size(max = 100, message = "Country of birth cannot exceed 100 characters")
    private String countryOfBirth;


    // ============================================================
    // ADDRESS
    // ============================================================

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;

    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String currentLivingCountry;


    // ============================================================
    // QUESTIONS
    // ============================================================

    private String question;

    private String comments;
}