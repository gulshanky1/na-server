package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.BlogStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogRequest {

    @NotBlank(message = "Blog title is required")
    @Size(max = 250, message = "Title cannot exceed 250 characters")
    private String title;

    @NotBlank(message = "Blog slug is required")
    @Size(max = 300, message = "Slug cannot exceed 300 characters")
    private String slug;

    @Size(max = 500, message = "Short description cannot exceed 500 characters")
    private String shortDescription;

    @NotBlank(message = "Blog content is required")
    private String content;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    @Size(max = 150, message = "Author cannot exceed 150 characters")
    private String author;

    @Size(max = 150, message = "Category cannot exceed 150 characters")
    private String category;

    @Size(max = 500, message = "Tags cannot exceed 500 characters")
    private String tags;

    @Size(max = 300, message = "Meta title cannot exceed 300 characters")
    private String metaTitle;

    @Size(max = 500, message = "Meta description cannot exceed 500 characters")
    private String metaDescription;

    private BlogStatus status;

    private Boolean featured;

    @Min(value = 1, message = "Read time must be at least 1 minute")
    private Integer readTime;
}