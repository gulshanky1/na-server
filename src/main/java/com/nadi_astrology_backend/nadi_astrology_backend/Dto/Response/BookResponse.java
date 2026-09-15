package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    private Long bookId;

    private String title;

    private String author;

    private String shortDescription;

    private String description;

    private String imageUrl;

    private BigDecimal price;

    private Integer stockQuantity;

    private String isbn;

    private String language;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}