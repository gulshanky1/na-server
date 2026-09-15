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
public class KundaliMilanDetailsResponse {

    private Long kundaliMilanId;

    private Long orderItemId;

    private String boyName;
    private LocalDate boyDateOfBirth;
    private LocalTime boyBirthTime;
    private String boyBirthPlace;

    private String girlName;
    private LocalDate girlDateOfBirth;
    private LocalTime girlBirthTime;
    private String girlBirthPlace;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}