package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.BlogStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository
        extends JpaRepository<Blog, Long> {
    long countByStatus(BlogStatus status);

    Optional<Blog> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<Blog> findByStatus(
            BlogStatus status,
            Pageable pageable
    );

    Page<Blog> findByStatusAndFeaturedTrue(
            BlogStatus status,
            Pageable pageable
    );

    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true
    )
    @Query("""
            UPDATE Blog b
            SET b.viewCount = b.viewCount + 1
            WHERE b.blogId = :blogId
            """)
    int incrementViewCount(
            @Param("blogId") Long blogId
    );
}