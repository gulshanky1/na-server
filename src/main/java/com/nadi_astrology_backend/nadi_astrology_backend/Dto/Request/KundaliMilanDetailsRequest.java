package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KundaliMilanDetailsRequest {

    // ============================================================
    // BOY DETAILS
    // ============================================================

    @NotBlank(message = "Boy name is required")
    @Size(
            max = 150,
            message = "Boy name cannot exceed 150 characters"
    )
    private String boyName;

    @NotNull(message = "Boy date of birth is required")
    private LocalDate boyDateOfBirth;

    @NotNull(message = "Boy birth time is required")
    @Past(message = "Boy date of birth must be in the past")
    private LocalTime boyBirthTime;

    @NotBlank(message = "Boy birth place is required")
    @Size(
            max = 150,
            message = "Boy birth place cannot exceed 150 characters"
    )
    private String boyBirthPlace;


    // ============================================================
    // GIRL DETAILS
    // ============================================================

    @NotBlank(message = "Girl name is required")
    @Size(
            max = 150,
            message = "Girl name cannot exceed 150 characters"
    )
    private String girlName;

    @NotNull(message = "Girl date of birth is required")
    @Past(message = "Girl date of birth must be in the past")
    private LocalDate girlDateOfBirth;

    @NotNull(message = "Girl birth time is required")
    private LocalTime girlBirthTime;

    @NotBlank(message = "Girl birth place is required")
    @Size(
            max = 150,
            message = "Girl birth place cannot exceed 150 characters"
    )
    private String girlBirthPlace;
}