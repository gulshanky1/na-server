package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.BlogRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.BlogResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.BlogStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.BlogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/blogs")
@RequiredArgsConstructor
@Validated
public class AdminBlogController {

    private final BlogService blogService;

    // =========================================================
    // CREATE BLOG
    // =========================================================

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<BlogResponse>> createBlog(

            @Valid @ModelAttribute BlogRequest request,

            @RequestPart(
                    value = "file",
                    required = false
            )
            MultipartFile file
    ) {

        BlogResponse blog =
                blogService.createBlog(
                        request,
                        file
                );

        ApiResponse<BlogResponse> response =
                new ApiResponse<>(
                        false,
                        201,
                        "Blog created successfully",
                        blog
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET ALL BLOGS
    // =========================================================

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BlogResponse>>> getAllBlogs(

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size cannot exceed 50")
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<BlogResponse> blogs =
                blogService.getAllBlogs(pageable);

        ApiResponse<Page<BlogResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Blogs fetched successfully",
                        blogs
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET BLOG BY ID
    // =========================================================

    @GetMapping("/{blogId}")
    public ResponseEntity<ApiResponse<BlogResponse>> getBlogById(

            @PathVariable
            @Positive(message = "Blog ID must be greater than 0")
            Long blogId
    ) {

        BlogResponse blog =
                blogService.getBlogById(blogId);

        ApiResponse<BlogResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Blog fetched successfully",
                        blog
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE BLOG
    // =========================================================

    @PutMapping("/{blogId}")
    public ResponseEntity<ApiResponse<BlogResponse>> updateBlog(

            @PathVariable
            @Positive(message = "Blog ID must be greater than 0")
            Long blogId,

            @Valid @RequestBody BlogRequest request
    ) {

        BlogResponse blog =
                blogService.updateBlog(
                        blogId,
                        request
                );

        ApiResponse<BlogResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Blog updated successfully",
                        blog
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPLOAD BLOG IMAGE
    // =========================================================

    @PostMapping(
            value = "/{blogId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<BlogResponse>> uploadImage(

            @PathVariable
            @Positive(message = "Blog ID must be greater than 0")
            Long blogId,

            @RequestPart("file")
            MultipartFile file
    ) {

        BlogResponse blog =
                blogService.uploadBlogImage(
                        blogId,
                        file
                );

        ApiResponse<BlogResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Blog image uploaded successfully",
                        blog
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // PUBLISH BLOG
    // =========================================================

    @PutMapping("/{blogId}/publish")
    public ResponseEntity<ApiResponse<BlogResponse>> publishBlog(

            @PathVariable
            @Positive(message = "Blog ID must be greater than 0")
            Long blogId
    ) {

        BlogResponse blog =
                blogService.publishBlog(blogId);

        ApiResponse<BlogResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Blog published successfully",
                        blog
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ARCHIVE BLOG
    // =========================================================

    @PutMapping("/{blogId}/archive")
    public ResponseEntity<ApiResponse<BlogResponse>> archiveBlog(

            @PathVariable
            @Positive(message = "Blog ID must be greater than 0")
            Long blogId
    ) {

        BlogResponse blog =
                blogService.archiveBlog(blogId);

        ApiResponse<BlogResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Blog archived successfully",
                        blog
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE BLOG
    // =========================================================

    @DeleteMapping("/{blogId}")
    public ResponseEntity<ApiResponse<Void>> deleteBlog(

            @PathVariable
            @Positive(message = "Blog ID must be greater than 0")
            Long blogId
    ) {

        blogService.deleteBlog(blogId);

        ApiResponse<Void> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Blog deleted successfully",
                        null
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // FILTER BLOGS BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<BlogResponse>>> getBlogsByStatus(

            @PathVariable BlogStatus status,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size cannot exceed 50")
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<BlogResponse> blogs =
                blogService.getBlogsByStatus(
                        status,
                        pageable
                );

        ApiResponse<Page<BlogResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Blogs fetched successfully",
                        blogs
                );

        return ResponseEntity.ok(response);
    }
}