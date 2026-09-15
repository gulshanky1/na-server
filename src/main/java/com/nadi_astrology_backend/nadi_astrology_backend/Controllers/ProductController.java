package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ProductResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    // ============================================================
    // GET ALL ACTIVE PRODUCTS
    // ============================================================

    @GetMapping
    public Page<ProductResponse> getProducts(
            Pageable pageable
    ) {

        return productService
                .getActiveProducts(pageable);
    }


    // ============================================================
    // GET ACTIVE PRODUCTS BY TYPE
    // ============================================================

    @GetMapping("/type/{type}")
    public Page<ProductResponse> getProductsByType(
            @PathVariable ProductType type,
            Pageable pageable
    ) {

        return productService
                .getActiveProductsByType(
                        type,
                        pageable
                );
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
}