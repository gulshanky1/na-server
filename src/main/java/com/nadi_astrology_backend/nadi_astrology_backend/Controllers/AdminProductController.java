package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ProductRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ProductResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
@Validated
public class AdminProductController {

    private final ProductService productService;

    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody ProductRequest request
    ) {

        return productService.createProduct(request);
    }

    // ============================================================
    // GET ALL PRODUCTS
    // ============================================================

    @GetMapping
    public Page<ProductResponse> getAllProducts(

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

        return productService.getAllProducts(pageable);
    }

    // ============================================================
    // GET PRODUCT BY ID
    // ============================================================

    @GetMapping("/{productId}")
    public ProductResponse getProductById(

            @PathVariable
            @Positive(message = "Product ID must be greater than 0")
            Long productId
    ) {

        return productService.getProductById(productId);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{productId}")
    public ProductResponse updateProduct(

            @PathVariable
            @Positive(message = "Product ID must be greater than 0")
            Long productId,

            @Valid @RequestBody ProductRequest request
    ) {

        return productService.updateProduct(
                productId,
                request
        );
    }

    // ============================================================
    // DEACTIVATE
    // ============================================================

    @DeleteMapping("/{productId}")
    public void deactivateProduct(

            @PathVariable
            @Positive(message = "Product ID must be greater than 0")
            Long productId
    ) {

        productService.deactivateProduct(productId);
    }

    // ============================================================
    // ACTIVATE
    // ============================================================

    @PatchMapping("/{productId}/activate")
    public ProductResponse activateProduct(

            @PathVariable
            @Positive(message = "Product ID must be greater than 0")
            Long productId
    ) {

        return productService.activateProduct(productId);
    }
}