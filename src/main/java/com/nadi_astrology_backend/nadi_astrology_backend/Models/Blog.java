package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.BlogStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "blogs",
        indexes = {
                @Index(
                        name = "idx_blog_slug",
                        columnList = "slug"
                ),
                @Index(
                        name = "idx_blog_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_blog_published_at",
                        columnList = "published_at"
                ),
                @Index(
                        name = "idx_blog_featured",
                        columnList = "featured"
                ),
                @Index(
                        name = "idx_blog_status_published",
                        columnList = "status, published_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;

    @Column(
            nullable = false,
            length = 250
    )
    private String title;

    @Column(
            nullable = false,
            unique = true,
            length = 300
    )
    private String slug;

    @Column(length = 500)
    private String shortDescription;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 150)
    private String author;

    @Column(length = 150)
    private String category;

    @Column(length = 500)
    private String tags;

    @Column(length = 300)
    private String metaTitle;

    @Column(length = 500)
    private String metaDescription;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    @Builder.Default
    private BlogStatus status = BlogStatus.DRAFT;

    @Column(nullable = false)
    @Builder.Default
    private boolean featured = false;

    @Column(nullable = false)
    @Builder.Default
    private long viewCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Integer readTime = 1;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = BlogStatus.DRAFT;
        }

        if (readTime == null || readTime < 1) {
            readTime = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();

        if (readTime == null || readTime < 1) {
            readTime = 1;
        }
    }
}