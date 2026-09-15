package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ProductRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ProductResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductTransformer {


    // ============================================================
    // REQUEST → ENTITY
    // ============================================================

    public Product toEntity(ProductRequest request) {

        return Product.builder()

                .type(request.getType())

                .referenceId(request.getReferenceId())

                .name(request.getName())

                .description(request.getDescription())

                .imageUrl(request.getImageUrl())

                .price(request.getPrice())

                .active(
                        request.getActive() == null
                                || request.getActive()
                )

                .build();
    }


    // ============================================================
    // UPDATE ENTITY
    // ============================================================

    public void updateEntity(
            Product product,
            ProductRequest request
    ) {

        product.setType(
                request.getType()
        );

        product.setReferenceId(
                request.getReferenceId()
        );

        product.setName(
                request.getName()
        );

        product.setDescription(
                request.getDescription()
        );

        product.setPrice(
                request.getPrice()
        );

        if (request.getImageUrl() != null) {

            product.setImageUrl(
                    request.getImageUrl()
            );
        }

        if (request.getActive() != null) {

            product.setActive(
                    request.getActive()
            );
        }
    }


    // ============================================================
    // ENTITY → RESPONSE
    // ============================================================

    public ProductResponse toResponse(Product product) {

        return ProductResponse.builder()

                .productId(
                        product.getProductId()
                )

                .type(
                        product.getType()
                )

                .referenceId(
                        product.getReferenceId()
                )

                .name(
                        product.getName()
                )

                .description(
                        product.getDescription()
                )

                .imageUrl(
                        product.getImageUrl()
                )

                .price(
                        product.getPrice()
                )

                .active(
                        product.isActive()
                )

                .createdAt(
                        product.getCreatedAt()
                )

                .updatedAt(
                        product.getUpdatedAt()
                )

                .build();
    }
}