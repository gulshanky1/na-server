package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ConsultationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneConsultationRequest {

    @NotBlank(message = "Consultation name is required")
    @Size(
            max = 200,
            message = "Consultation name cannot exceed 200 characters"
    )
    private String name;

    @NotNull(message = "Consultation type is required")
    private ConsultationType consultationType;

    @Size(
            max = 500,
            message = "Short description cannot exceed 500 characters"
    )
    private String shortDescription;

    @NotBlank(message = "Consultation description is required")
    private String description;

    @Size(
            max = 500,
            message = "Image URL cannot exceed 500 characters"
    )
    private String imageUrl;

    @NotNull(message = "Duration is required")
    @Min(
            value = 1,
            message = "Duration must be at least 1 minute"
    )
    private Integer durationMinutes;

    @NotNull(message = "Consultation price is required")
    @DecimalMin(
            value = "0.01",
            message = "Consultation price must be greater than 0"
    )
    private BigDecimal price;

    private Boolean active;
}