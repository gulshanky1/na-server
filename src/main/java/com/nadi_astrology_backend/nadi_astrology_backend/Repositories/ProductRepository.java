package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByActiveTrue(Pageable pageable);

    Page<Product> findByTypeAndActiveTrue(
            ProductType type,
            Pageable pageable
    );

    boolean existsByTypeAndReferenceId(
            ProductType type,
            Long referenceId
    );

    long countByActiveTrue();

    long countByType(ProductType type);

    Optional<Product> findByTypeAndReferenceId(
            ProductType type,
            Long referenceId
    );

    Optional<Product> findByProductIdAndActiveTrue(
            Long productId
    );
}