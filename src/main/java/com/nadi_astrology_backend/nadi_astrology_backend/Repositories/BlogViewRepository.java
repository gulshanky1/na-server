package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.BlogView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogViewRepository
        extends JpaRepository<BlogView, Long> {

    @Modifying
    @Query(value = """
        INSERT INTO blog_views
            (blog_id, viewer_type, viewer_key, viewed_at)
        VALUES
            (:blogId, :viewerType, :viewerKey, CURRENT_TIMESTAMP)
        ON CONFLICT (blog_id, viewer_type, viewer_key)
        DO NOTHING
        """, nativeQuery = true)
    int insertIfNotExists(
            @Param("blogId") Long blogId,
            @Param("viewerType") String viewerType,
            @Param("viewerKey") String viewerKey
    );
}