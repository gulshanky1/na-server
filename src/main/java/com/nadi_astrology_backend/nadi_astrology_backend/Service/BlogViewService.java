package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.Blog;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.BlogViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogViewService {

    private final BlogViewRepository blogViewRepository;

    @Transactional
    public boolean recordView(
            Blog blog,
            String viewerType,
            String viewerKey
    ) {

        if (blog == null ||
                viewerType == null ||
                viewerKey == null ||
                viewerType.isBlank() ||
                viewerKey.isBlank()) {

            return false;
        }

        String type = viewerType.trim().toUpperCase();
        String key = viewerKey.trim();

        int inserted = blogViewRepository.insertIfNotExists(
                blog.getBlogId(),
                type,
                key
        );

        return inserted == 1;
    }
}