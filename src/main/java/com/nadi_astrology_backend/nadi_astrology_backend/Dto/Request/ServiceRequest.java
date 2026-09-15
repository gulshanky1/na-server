package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import jakarta.validation.constraints.DecimalMin;
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
public class ServiceRequest {

    @NotBlank(message = "Service name is required")
    @Size(
            max = 200,
            message = "Service name cannot exceed 200 characters"
    )
    private String name;

    @Size(
            max = 500,
            message = "Short description cannot exceed 500 characters"
    )
    private String shortDescription;

    @NotBlank(message = "Service description is required")
    private String description;

    @Size(
            max = 500,
            message = "Image URL cannot exceed 500 characters"
    )
    private String imageUrl;

    @NotNull(message = "Service price is required")
    @DecimalMin(
            value = "0.01",
            message = "Service price must be greater than 0"
    )
    private BigDecimal price;

    private ServiceType serviceType;

    private Boolean active;
}