package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.BookResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * Get all active books
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getActiveBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<BookResponse> books =
                bookService.getActiveBooks(pageable);

        ApiResponse<Page<BookResponse>> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Books fetched successfully",
                        books
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Get active book by ID
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(
            @PathVariable Long bookId
    ) {

        BookResponse book =
                bookService.getBookById(bookId);

        ApiResponse<BookResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Book fetched successfully",
                        book
                );

        return ResponseEntity.ok(response);
    }
}