package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.BlogStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogResponse {

    private Long blogId;

    private String title;

    private String slug;

    private String shortDescription;

    private String content;

    private String imageUrl;

    private String author;

    private String category;

    private String tags;

    private String metaTitle;

    private String metaDescription;

    private BlogStatus status;

    private boolean featured;

    private long viewCount;

    private Integer readTime;

    private LocalDateTime publishedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}