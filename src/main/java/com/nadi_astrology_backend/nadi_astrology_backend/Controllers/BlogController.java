package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.BlogResponse;

import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.BlogService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;


    // =========================================================
    // GET PUBLISHED BLOGS
    // =========================================================

    @GetMapping
    public ResponseEntity<
            ApiResponse<Page<BlogResponse>>
            > getPublishedBlogs(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<BlogResponse> blogs =
                blogService.getPublishedBlogs(
                        pageable
                );

        ApiResponse<Page<BlogResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Published blogs fetched successfully",
                        blogs
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET FEATURED BLOGS
    // =========================================================

    @GetMapping("/featured")
    public ResponseEntity<
            ApiResponse<Page<BlogResponse>>
            > getFeaturedBlogs(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<BlogResponse> blogs =
                blogService.getFeaturedBlogs(
                        pageable
                );

        ApiResponse<Page<BlogResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Featured blogs fetched successfully",
                        blogs
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET BLOG BY SLUG
    // =========================================================

    @GetMapping("/{slug}")
    public ResponseEntity<
            ApiResponse<BlogResponse>
            > getBlogBySlug(

            @PathVariable String slug,

            HttpServletRequest request,

            HttpServletResponse response
    ) {

        String viewerType;

        String viewerKey;


        // =====================================================
        // CHECK LOGGED-IN USER
        // =====================================================

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication != null
                        && authentication.isAuthenticated()
                        && authentication.getPrincipal()
                        instanceof AuthenticatedUser authenticatedUser
        ) {

            /*
             * Logged-in user
             */
            viewerType = "USER";

            viewerKey =
                    String.valueOf(
                            authenticatedUser.getUserId()
                    );

        }


        // =====================================================
        // ANONYMOUS VISITOR
        // =====================================================

        else {

            viewerType = "VISITOR";

            String visitorId =
                    getVisitorId(request);


            /*
             * First visit from this browser.
             */
            if (
                    visitorId == null
                            || visitorId.isBlank()
            ) {

                visitorId =
                        UUID.randomUUID()
                                .toString();


                Cookie cookie =
                        new Cookie(
                                "BLOG_VISITOR_ID",
                                visitorId
                        );

                /*
                 * Development:
                 * localhost is HTTP.
                 *
                 * For production HTTPS,
                 * change this to true.
                 */
                cookie.setSecure(false);

                /*
                 * JavaScript cannot read this cookie.
                 */
                cookie.setHttpOnly(true);

                cookie.setPath("/");

                /*
                 * Keep visitor identity
                 * for one year.
                 */
                cookie.setMaxAge(
                        60 * 60 * 24 * 365
                );

                response.addCookie(cookie);
            }

            viewerKey = visitorId;
        }


        // =====================================================
        // FETCH BLOG + RECORD UNIQUE VIEW
        // =====================================================

        BlogResponse blog =
                blogService.getPublishedBlogBySlug(
                        slug,
                        viewerType,
                        viewerKey
                );


        // =====================================================
        // API RESPONSE
        // =====================================================

        ApiResponse<BlogResponse> apiResponse =
                new ApiResponse<>(
                        false,
                        200,
                        "Blog fetched successfully",
                        blog
                );

        return ResponseEntity.ok(
                apiResponse
        );
    }


    // =========================================================
    // GET VISITOR ID FROM COOKIE
    // =========================================================

    private String getVisitorId(
            HttpServletRequest request
    ) {

        Cookie[] cookies =
                request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {

            if (
                    "BLOG_VISITOR_ID"
                            .equals(cookie.getName())
            ) {

                return cookie.getValue();
            }
        }

        return null;
    }
}