package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @NotBlank(message = "Book title is required")
    @Size(max = 200, message = "Book title cannot exceed 200 characters")
    private String title;

    @Size(max = 150, message = "Author name cannot exceed 150 characters")
    private String author;

    @Size(max = 500, message = "Short description cannot exceed 500 characters")
    private String shortDescription;

    @NotBlank(message = "Book description is required")
    private String description;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    @NotNull(message = "Book price is required")
    @DecimalMin(
            value = "0.01",
            message = "Book price must be greater than 0"
    )
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @Size(max = 100, message = "ISBN cannot exceed 100 characters")
    private String isbn;

    @Size(max = 100, message = "Language cannot exceed 100 characters")
    private String language;

    private Boolean active;
}