package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Transformer;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.BookRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.BookResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Book;
import org.springframework.stereotype.Component;

@Component
public class BookTransformer {

    public Book toEntity(BookRequest request) {

        return Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .shortDescription(request.getShortDescription())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .isbn(request.getIsbn())
                .language(request.getLanguage())
                .active(
                        request.getActive() != null
                                ? request.getActive()
                                : true
                )
                .build();
    }

    public void updateEntity(Book book, BookRequest request) {

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setShortDescription(request.getShortDescription());
        book.setDescription(request.getDescription());
        book.setImageUrl(request.getImageUrl());
        book.setPrice(request.getPrice());
        book.setStockQuantity(request.getStockQuantity());
        book.setIsbn(request.getIsbn());
        book.setLanguage(request.getLanguage());

        if (request.getActive() != null) {
            book.setActive(request.getActive());
        }
    }

    public BookResponse toResponse(Book book) {

        return BookResponse.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .shortDescription(book.getShortDescription())
                .description(book.getDescription())
                .imageUrl(book.getImageUrl())
                .price(book.getPrice())
                .stockQuantity(book.getStockQuantity())
                .isbn(book.getIsbn())
                .language(book.getLanguage())
                .active(book.isActive())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}