package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ProductRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ProductResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ProductRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.ProductTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ProductService {


    private final ProductRepository productRepository;

    private final ProductTransformer productTransformer;


    // ============================================================
    // CREATE PRODUCT
    // ============================================================

    public ProductResponse createProduct(
            ProductRequest request
    ) {

        boolean exists =
                productRepository.existsByTypeAndReferenceId(
                        request.getType(),
                        request.getReferenceId()
                );

        if (exists) {

            throw new IllegalArgumentException(
                    "Product already exists for type "
                            + request.getType()
                            + " and referenceId "
                            + request.getReferenceId()
            );
        }

        Product product =
                productTransformer.toEntity(request);

        Product saved =
                productRepository.save(product);

        return productTransformer.toResponse(saved);
    }


    // ============================================================
    // GET PRODUCT BY ID
    // ============================================================

    public ProductResponse getProductById(
            Long productId
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Product not found with id: "
                                                + productId
                                )
                        );

        return productTransformer.toResponse(product);
    }


    // ============================================================
    // GET ACTIVE PRODUCTS
    // ============================================================

    public Page<ProductResponse> getActiveProducts(
            Pageable pageable
    ) {

        return productRepository
                .findByActiveTrue(pageable)
                .map(productTransformer::toResponse);
    }


    // ============================================================
    // GET ACTIVE PRODUCTS BY TYPE
    // ============================================================

    public Page<ProductResponse> getActiveProductsByType(
            ProductType type,
            Pageable pageable
    ) {

        return productRepository
                .findByTypeAndActiveTrue(
                        type,
                        pageable
                )
                .map(productTransformer::toResponse);
    }


    // ============================================================
    // GET ALL PRODUCTS - ADMIN
    // ============================================================

    public Page<ProductResponse> getAllProducts(
            Pageable pageable
    ) {

        return productRepository
                .findAll(pageable)
                .map(productTransformer::toResponse);
    }


    // ============================================================
    // UPDATE PRODUCT
    // ============================================================

    public ProductResponse updateProduct(
            Long productId,
            ProductRequest request
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Product not found with id: "
                                                + productId
                                )
                        );


        productRepository
                .findByTypeAndReferenceId(
                        request.getType(),
                        request.getReferenceId()
                )
                .ifPresent(existing -> {

                    if (!existing.getProductId()
                            .equals(productId)) {

                        throw new IllegalArgumentException(
                                "Another product already exists for type "
                                        + request.getType()
                                        + " and referenceId "
                                        + request.getReferenceId()
                        );
                    }
                });


        productTransformer.updateEntity(
                product,
                request
        );

        Product updated =
                productRepository.save(product);

        return productTransformer.toResponse(updated);
    }


    // ============================================================
    // DEACTIVATE PRODUCT
    // ============================================================

    public void deactivateProduct(
            Long productId
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Product not found with id: "
                                                + productId
                                )
                        );

        product.setActive(false);

        productRepository.save(product);
    }


    // ============================================================
    // ACTIVATE PRODUCT
    // ============================================================

    public ProductResponse activateProduct(
            Long productId
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Product not found with id: "
                                                + productId
                                )
                        );

        product.setActive(true);

        Product saved =
                productRepository.save(product);

        return productTransformer.toResponse(saved);
    }
}