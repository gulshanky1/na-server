package com.nadi_astrology_backend.nadi_astrology_backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "blog_views",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_blog_viewer",
                        columnNames = {"blog_id", "viewer_type", "viewer_key"}
                )
        },
        indexes = {
                @Index(name = "idx_blog_views_blog", columnList = "blog_id"),
                @Index(name = "idx_blog_views_viewed_at", columnList = "viewed_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogViewId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "blog_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_blog_view_blog")
    )
    private Blog blog;

    /**
     * USER      -> logged-in user
     * VISITOR   -> anonymous browser visitor
     */
    @Column(name = "viewer_type", nullable = false, length = 20)
    private String viewerType;

    /**
     * For USER:
     *      userId as String
     *
     * For VISITOR:
     *      random UUID stored in browser cookie
     */
    @Column(name = "viewer_key", nullable = false, length = 100)
    private String viewerKey;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;

    @PrePersist
    protected void onCreate() {
        if (viewedAt == null) {
            viewedAt = LocalDateTime.now();
        }
    }
}