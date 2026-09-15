package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "books",
        indexes = {
                @Index(name = "idx_book_active", columnList = "active"),
                @Index(name = "idx_book_title", columnList = "title")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 150)
    private String author;

    @Column(length = 500)
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(length = 100)
    private String isbn;

    @Column(length = 100)
    private String language;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}