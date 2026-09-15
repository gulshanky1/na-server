package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotNull(message = "Product type is required")
    private ProductType type;


    @NotNull(message = "Reference ID is required")
    @Positive(message = "Reference ID must be greater than 0")
    private Long referenceId;


    @NotBlank(message = "Product name is required")
    @Size(
            max = 200,
            message = "Product name cannot exceed 200 characters"
    )
    private String name;


    @Size(
            max = 1000,
            message = "Product description cannot exceed 1000 characters"
    )
    private String description;


    @Size(
            max = 500,
            message = "Image URL cannot exceed 500 characters"
    )
    private String imageUrl;


    @NotNull(message = "Product price is required")
    @DecimalMin(
            value = "0.01",
            message = "Product price must be greater than 0"
    )
    private BigDecimal price;


    private Boolean active;
}