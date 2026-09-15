package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.BookRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.BookResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;

    /**
     * Create a new book
     */
    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookRequest request
    ) {

        BookResponse response = bookService.createBook(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get all books including inactive books
     */
    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                bookService.getAllBooks(pageable)
        );
    }

    /**
     * Get a book by ID
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable Long bookId
    ) {

        return ResponseEntity.ok(
                bookService.getBookById(bookId)
        );
    }

    /**
     * Update a book
     */
    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long bookId,
            @Valid @RequestBody BookRequest request
    ) {

        return ResponseEntity.ok(
                bookService.updateBook(bookId, request)
        );
    }

    /**
     * Deactivate a book
     */
    @DeleteMapping("/{bookId}")
    public ResponseEntity<BookResponse> deactivateBook(
            @PathVariable Long bookId
    ) {

        return ResponseEntity.ok(
                bookService.deactivateBook(bookId)
        );
    }

    /**
     * Upload book image to Cloudinary
     */
    @PostMapping(
            value = "/{bookId}/image",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<BookResponse> uploadBookImage(
            @PathVariable Long bookId,
            @RequestParam("file") MultipartFile file
    ) {

        BookResponse response =
                bookService.uploadBookImage(bookId, file);

        return ResponseEntity.ok(response);
    }
}