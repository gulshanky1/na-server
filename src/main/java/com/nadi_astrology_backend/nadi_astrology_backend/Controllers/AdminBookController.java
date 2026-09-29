package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.BookRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.BookResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/books")
@RequiredArgsConstructor
@Validated
public class AdminBookController {

    private final BookService bookService;

    /**
     * Create a new book
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(
            @Valid @RequestBody BookRequest request
    ) {

        BookResponse book =
                bookService.createBook(request);

        ApiResponse<BookResponse> response =
                new ApiResponse<>(
                        false,
                        201,
                        "Book created successfully",
                        book
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get all books including inactive books
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getAllBooks(

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

        Page<BookResponse> books =
                bookService.getAllBooks(pageable);

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
     * Get a book by ID
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(

            @PathVariable
            @Positive(message = "Book ID must be greater than 0")
            Long bookId
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

    /**
     * Update a book
     */
    @PutMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(

            @PathVariable
            @Positive(message = "Book ID must be greater than 0")
            Long bookId,

            @Valid @RequestBody BookRequest request
    ) {

        BookResponse book =
                bookService.updateBook(
                        bookId,
                        request
                );

        ApiResponse<BookResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Book updated successfully",
                        book
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Deactivate a book
     */
    @DeleteMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> deactivateBook(

            @PathVariable
            @Positive(message = "Book ID must be greater than 0")
            Long bookId
    ) {

        BookResponse book =
                bookService.deactivateBook(bookId);

        ApiResponse<BookResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Book deactivated successfully",
                        book
                );

        return ResponseEntity.ok(response);
    }

    /**
     * Upload book image to Cloudinary
     */
    @PostMapping(
            value = "/{bookId}/image",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ApiResponse<BookResponse>> uploadBookImage(

            @PathVariable
            @Positive(message = "Book ID must be greater than 0")
            Long bookId,

            @RequestParam("file")
            MultipartFile file
    ) {

        BookResponse book =
                bookService.uploadBookImage(
                        bookId,
                        file
                );

        ApiResponse<BookResponse> response =
                new ApiResponse<>(
                        false,
                        200,
                        "Book image uploaded successfully",
                        book
                );

        return ResponseEntity.ok(response);
    }
}