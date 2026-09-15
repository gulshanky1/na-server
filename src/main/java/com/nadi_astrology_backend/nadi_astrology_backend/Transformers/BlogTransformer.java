package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.BlogRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.BlogResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.BlogStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Blog;
import org.springframework.stereotype.Component;

@Component
public class BlogTransformer {


    // =========================================================
    // REQUEST -> ENTITY
    // =========================================================

    public Blog toEntity(
            BlogRequest request
    ) {

        return Blog.builder()

                .title(
                        request.getTitle()
                                .trim()
                )

                .slug(
                        request.getSlug()
                                .trim()
                                .toLowerCase()
                )

                .shortDescription(
                        request.getShortDescription()
                )

                .content(
                        request.getContent()
                )

                .imageUrl(
                        request.getImageUrl()
                )

                .author(
                        request.getAuthor()
                )

                .category(
                        request.getCategory()
                )

                .tags(
                        request.getTags()
                )

                .metaTitle(
                        request.getMetaTitle()
                )

                .metaDescription(
                        request.getMetaDescription()
                )

                .status(
                        request.getStatus() != null
                                ? request.getStatus()
                                : BlogStatus.DRAFT
                )

                .featured(
                        request.getFeatured() != null
                                && request.getFeatured()
                )

                .readTime(
                        request.getReadTime() != null
                                ? request.getReadTime()
                                : 1
                )

                .build();
    }


    // =========================================================
    // UPDATE ENTITY
    // =========================================================

    public void updateEntity(
            Blog blog,
            BlogRequest request
    ) {

        blog.setTitle(
                request.getTitle()
                        .trim()
        );

        blog.setSlug(
                request.getSlug()
                        .trim()
                        .toLowerCase()
        );

        blog.setShortDescription(
                request.getShortDescription()
        );

        blog.setContent(
                request.getContent()
        );

        blog.setImageUrl(
                request.getImageUrl()
        );

        blog.setAuthor(
                request.getAuthor()
        );

        blog.setCategory(
                request.getCategory()
        );

        blog.setTags(
                request.getTags()
        );

        blog.setMetaTitle(
                request.getMetaTitle()
        );

        blog.setMetaDescription(
                request.getMetaDescription()
        );


        if (request.getStatus() != null) {

            blog.setStatus(
                    request.getStatus()
            );
        }


        if (request.getFeatured() != null) {

            blog.setFeatured(
                    request.getFeatured()
            );
        }


        if (request.getReadTime() != null) {

            blog.setReadTime(
                    request.getReadTime()
            );
        }
    }


    // =========================================================
    // ENTITY -> RESPONSE
    // =========================================================

    public BlogResponse toResponse(
            Blog blog
    ) {

        return BlogResponse.builder()

                .blogId(
                        blog.getBlogId()
                )

                .title(
                        blog.getTitle()
                )

                .slug(
                        blog.getSlug()
                )

                .shortDescription(
                        blog.getShortDescription()
                )

                .content(
                        blog.getContent()
                )

                .imageUrl(
                        blog.getImageUrl()
                )

                .author(
                        blog.getAuthor()
                )

                .category(
                        blog.getCategory()
                )

                .tags(
                        blog.getTags()
                )

                .metaTitle(
                        blog.getMetaTitle()
                )

                .metaDescription(
                        blog.getMetaDescription()
                )

                .status(
                        blog.getStatus()
                )

                .featured(
                        blog.isFeatured()
                )

                .viewCount(
                        blog.getViewCount()
                )

                .readTime(
                        blog.getReadTime()
                )

                .publishedAt(
                        blog.getPublishedAt()
                )

                .createdAt(
                        blog.getCreatedAt()
                )

                .updatedAt(
                        blog.getUpdatedAt()
                )

                .build();
    }
}