package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.BlogRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.BlogResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.BlogStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.DuplicateResourceException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Blog;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.BlogRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.BlogTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    private final BlogTransformer blogTransformer;

    private final CloudinaryService cloudinaryService;

    private final BlogViewService blogViewService;


    // =========================================================
    // ADMIN - GET ALL BLOGS
    // =========================================================

    @Transactional(readOnly = true)
    public Page<BlogResponse> getAllBlogs(
            Pageable pageable
    ) {

        return blogRepository
                .findAll(pageable)
                .map(blogTransformer::toResponse);
    }


    // =========================================================
    // ADMIN - GET BLOG BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public BlogResponse getBlogById(
            Long blogId
    ) {

        Blog blog =
                blogRepository.findById(blogId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Blog not found with id: " + blogId
                                )
                        );

        return blogTransformer.toResponse(blog);
    }


    // =========================================================
    // ADMIN - CREATE BLOG
    // =========================================================

    // =========================================================
// ADMIN - CREATE BLOG
// =========================================================

    @Transactional
    public BlogResponse createBlog(
            BlogRequest request,
            MultipartFile file
    ) {

        String slug =
                request.getSlug()
                        .trim()
                        .toLowerCase();

        // ---------------------------------------------------------
        // CHECK DUPLICATE SLUG
        // ---------------------------------------------------------

        if (blogRepository.existsBySlug(slug)) {

            throw new DuplicateResourceException(
                    "Blog already exists with slug: " + slug
            );
        }

        // ---------------------------------------------------------
        // CONVERT REQUEST TO ENTITY
        // ---------------------------------------------------------

        Blog blog =
                blogTransformer.toEntity(request);

        // ---------------------------------------------------------
        // UPLOAD IMAGE IF PROVIDED
        // ---------------------------------------------------------

        if (file != null && !file.isEmpty()) {

            String imageUrl =
                    cloudinaryService.uploadImage(file);

            blog.setImageUrl(imageUrl);
        }

        // ---------------------------------------------------------
        // SAVE BLOG
        // ---------------------------------------------------------

        Blog savedBlog =
                blogRepository.save(blog);

        // ---------------------------------------------------------
        // RESPONSE
        // ---------------------------------------------------------

        return blogTransformer.toResponse(
                savedBlog
        );
    }
    // =========================================================
// ADMIN - GET BLOGS BY STATUS
// =========================================================

    @Transactional(readOnly = true)
    public Page<BlogResponse> getBlogsByStatus(
            BlogStatus status,
            Pageable pageable
    ) {

        return blogRepository
                .findByStatus(
                        status,
                        pageable
                )
                .map(blogTransformer::toResponse);
    }



    // =========================================================
    // ADMIN - UPDATE BLOG
    // =========================================================

    @Transactional
    public BlogResponse updateBlog(
            Long blogId,
            BlogRequest request
    ) {

        Blog blog =
                blogRepository.findById(blogId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Blog not found with id: " + blogId
                                )
                        );

        String newSlug =
                request.getSlug()
                        .trim()
                        .toLowerCase();

        if (!blog.getSlug().equals(newSlug)
                && blogRepository.existsBySlug(newSlug)) {

            throw new DuplicateResourceException(
                    "Blog already exists with slug: " + newSlug
            );
        }

        blogTransformer.updateEntity(
                blog,
                request
        );

        Blog savedBlog =
                blogRepository.save(blog);

        return blogTransformer.toResponse(savedBlog);
    }


    // =========================================================
    // ADMIN - UPLOAD IMAGE
    // =========================================================

    @Transactional
    public BlogResponse uploadBlogImage(
            Long blogId,
            MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {

            throw new BadRequestException(
                    "Blog image file is required"
            );
        }

        Blog blog =
                blogRepository.findById(blogId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Blog not found with id: " + blogId
                                )
                        );

        String imageUrl =
                cloudinaryService.uploadImage(file);

        blog.setImageUrl(imageUrl);

        Blog savedBlog =
                blogRepository.save(blog);

        return blogTransformer.toResponse(savedBlog);
    }


    // =========================================================
    // ADMIN - PUBLISH BLOG
    // =========================================================

    @Transactional
    public BlogResponse publishBlog(
            Long blogId
    ) {

        Blog blog =
                blogRepository.findById(blogId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Blog not found with id: " + blogId
                                )
                        );

        if (blog.getContent() == null
                || blog.getContent().isBlank()) {

            throw new BadRequestException(
                    "Blog content is required before publishing"
            );
        }

        blog.setStatus(
                BlogStatus.PUBLISHED
        );

        if (blog.getPublishedAt() == null) {

            blog.setPublishedAt(
                    LocalDateTime.now()
            );
        }

        Blog savedBlog =
                blogRepository.save(blog);

        return blogTransformer.toResponse(savedBlog);
    }


    // =========================================================
    // ADMIN - ARCHIVE BLOG
    // =========================================================

    @Transactional
    public BlogResponse archiveBlog(
            Long blogId
    ) {

        Blog blog =
                blogRepository.findById(blogId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Blog not found with id: " + blogId
                                )
                        );

        blog.setStatus(
                BlogStatus.ARCHIVED
        );

        Blog savedBlog =
                blogRepository.save(blog);

        return blogTransformer.toResponse(savedBlog);
    }


    // =========================================================
    // ADMIN - DELETE BLOG
    // =========================================================

    @Transactional
    public void deleteBlog(
            Long blogId
    ) {

        Blog blog =
                blogRepository.findById(blogId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Blog not found with id: " + blogId
                                )
                        );

        blogRepository.delete(blog);
    }


    // =========================================================
    // PUBLIC - GET PUBLISHED BLOGS
    // =========================================================

    @Transactional(readOnly = true)
    public Page<BlogResponse> getPublishedBlogs(
            Pageable pageable
    ) {

        return blogRepository
                .findByStatus(
                        BlogStatus.PUBLISHED,
                        pageable
                )
                .map(blogTransformer::toResponse);
    }


    // =========================================================
    // PUBLIC - GET FEATURED BLOGS
    // =========================================================

    @Transactional(readOnly = true)
    public Page<BlogResponse> getFeaturedBlogs(
            Pageable pageable
    ) {

        return blogRepository
                .findByStatusAndFeaturedTrue(
                        BlogStatus.PUBLISHED,
                        pageable
                )
                .map(blogTransformer::toResponse);
    }


    // =========================================================
    // PUBLIC - GET BLOG BY SLUG
    // =========================================================

    @Transactional
    public BlogResponse getPublishedBlogBySlug(
            String slug,
            String viewerType,
            String viewerKey
    ) {

        Blog blog =
                blogRepository.findBySlug(
                                slug.trim().toLowerCase()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Blog not found"
                                )
                        );

        if (blog.getStatus()
                != BlogStatus.PUBLISHED) {

            throw new ResourceNotFoundException(
                    "Blog not found"
            );
        }


        // -----------------------------------------------------
        // RECORD UNIQUE VIEW
        // -----------------------------------------------------

        boolean newView =
                blogViewService.recordView(
                        blog,
                        viewerType,
                        viewerKey
                );


        // -----------------------------------------------------
        // INCREMENT VIEW COUNT ONLY FOR NEW VIEW
        // -----------------------------------------------------

        if (newView) {

            blogRepository.incrementViewCount(
                    blog.getBlogId()
            );


            // Reload latest view count
            blog =
                    blogRepository.findById(
                            blog.getBlogId()
                    ).orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Blog not found"
                            )
                    );
        }


        return blogTransformer.toResponse(
                blog
        );
    }
}