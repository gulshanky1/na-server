package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.BookRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.BookResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Transformer.BookTransformer;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.DuplicateResourceException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Book;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransformer bookTransformer;
    private final CloudinaryService cloudinaryService;
    private final ProductSyncService productSyncService;


    // =========================================================
    // CREATE BOOK
    // =========================================================

    @Transactional
    public BookResponse createBook(BookRequest request) {

        String title = request.getTitle().trim();

        if (bookRepository.existsByTitleIgnoreCase(title)) {
            throw new DuplicateResourceException(
                    "Book already exists with title: " + title
            );
        }

        Book book = bookTransformer.toEntity(request);

        Book savedBook = bookRepository.save(book);

        /*
         * Automatically create/update Product.
         */
        productSyncService.createOrUpdateProduct(
                ProductType.BOOK,
                savedBook.getBookId(),
                savedBook.getTitle(),
                savedBook.getShortDescription(),
                savedBook.getImageUrl(),
                savedBook.getPrice(),
                savedBook.isActive()
        );

        return bookTransformer.toResponse(savedBook);
    }


    // =========================================================
    // GET BOOK BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public BookResponse getBookById(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );

        return bookTransformer.toResponse(book);
    }


    // =========================================================
    // GET ACTIVE BOOKS
    // =========================================================

    @Transactional(readOnly = true)
    public Page<BookResponse> getActiveBooks(Pageable pageable) {

        return bookRepository.findByActiveTrue(pageable)
                .map(bookTransformer::toResponse);
    }


    // =========================================================
    // GET ALL BOOKS
    // =========================================================

    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(Pageable pageable) {

        return bookRepository.findAll(pageable)
                .map(bookTransformer::toResponse);
    }


    // =========================================================
    // UPDATE BOOK
    // =========================================================

    @Transactional
    public BookResponse updateBook(
            Long bookId,
            BookRequest request
    ) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );

        String title = request.getTitle().trim();

        /*
         * Check duplicate title only if title changed.
         */
        if (!book.getTitle().equalsIgnoreCase(title)
                && bookRepository.existsByTitleIgnoreCase(title)) {

            throw new DuplicateResourceException(
                    "Another book already exists with title: " + title
            );
        }

        bookTransformer.updateEntity(book, request);

        Book updatedBook = bookRepository.save(book);

        /*
         * Synchronize Product after update.
         */
        productSyncService.createOrUpdateProduct(
                ProductType.BOOK,
                updatedBook.getBookId(),
                updatedBook.getTitle(),
                updatedBook.getShortDescription(),
                updatedBook.getImageUrl(),
                updatedBook.getPrice(),
                updatedBook.isActive()
        );

        return bookTransformer.toResponse(updatedBook);
    }


    // =========================================================
    // DEACTIVATE BOOK
    // =========================================================

    @Transactional
    public BookResponse deactivateBook(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );

        book.setActive(false);

        Book savedBook = bookRepository.save(book);

        /*
         * Also deactivate the Product.
         */
        productSyncService.deactivateProduct(
                ProductType.BOOK,
                bookId
        );

        return bookTransformer.toResponse(savedBook);
    }


    // =========================================================
    // UPLOAD BOOK IMAGE
    // =========================================================

    @Transactional
    public BookResponse uploadBookImage(
            Long bookId,
            MultipartFile file
    ) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );

        String imageUrl = cloudinaryService.uploadImage(file);

        book.setImageUrl(imageUrl);

        Book savedBook = bookRepository.save(book);

        /*
         * Update Product image as well.
         */
        productSyncService.createOrUpdateProduct(
                ProductType.BOOK,
                savedBook.getBookId(),
                savedBook.getTitle(),
                savedBook.getShortDescription(),
                savedBook.getImageUrl(),
                savedBook.getPrice(),
                savedBook.isActive()
        );

        return bookTransformer.toResponse(savedBook);
    }
}