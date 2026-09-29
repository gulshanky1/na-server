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
public class AdminCustomerInformationResponse {

    private Long customerInformationId;

    private String fullName;
    private String email;
    private String phone;

    private LocalDate dateOfBirth;
    private LocalTime birthTime;
    private String birthPlace;
    private String countryOfBirth;

    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String currentLivingCountry;

    private String question;
    private String comments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}