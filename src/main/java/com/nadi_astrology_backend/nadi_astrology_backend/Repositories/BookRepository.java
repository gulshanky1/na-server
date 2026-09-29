package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByActiveTrue(Pageable pageable);

    boolean existsByTitleIgnoreCase(String title);

    Optional<Book> findByTitleIgnoreCase(String title);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT b
        FROM Book b
        WHERE b.bookId = :bookId
        """)
    Optional<Book> findByIdForUpdate(
            @Param("bookId") Long bookId
    );

}