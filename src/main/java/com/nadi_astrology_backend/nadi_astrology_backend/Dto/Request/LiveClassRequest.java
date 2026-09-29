package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.LiveClassStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiveClassRequest {

    @NotNull
    @Positive(message = "Course ID must be greater than 0")
    private Long courseId;

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    private LocalDate classDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private LiveClassStatus status;
}