package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ProductRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ProductResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;


    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody ProductRequest request
    ) {

        return productService
                .createProduct(request);
    }


    // ============================================================
    // GET ALL PRODUCTS
    // ============================================================

    @GetMapping
    public Page<ProductResponse> getAllProducts(
            Pageable pageable
    ) {

        return productService
                .getAllProducts(pageable);
    }


    // ============================================================
    // GET PRODUCT BY ID
    // ============================================================

    @GetMapping("/{productId}")
    public ProductResponse getProductById(
            @PathVariable Long productId
    ) {

        return productService
                .getProductById(productId);
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{productId}")
    public ProductResponse updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request
    ) {

        return productService
                .updateProduct(
                        productId,
                        request
                );
    }


    // ============================================================
    // DEACTIVATE
    // ============================================================

    @DeleteMapping("/{productId}")
    public void deactivateProduct(
            @PathVariable Long productId
    ) {

        productService
                .deactivateProduct(productId);
    }


    // ============================================================
    // ACTIVATE
    // ============================================================

    @PatchMapping("/{productId}/activate")
    public ProductResponse activateProduct(
            @PathVariable Long productId
    ) {

        return productService
                .activateProduct(productId);
    }
}