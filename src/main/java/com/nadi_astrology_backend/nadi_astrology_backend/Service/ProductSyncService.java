package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductSyncService {

    private final ProductRepository productRepository;

    public Product createOrUpdateProduct(
            ProductType type,
            Long referenceId,
            String name,
            String description,
            String imageUrl,
            BigDecimal price,
            boolean active
    ) {

        Product product = productRepository
                .findByTypeAndReferenceId(type, referenceId)
                .orElseGet(Product::new);

        product.setType(type);
        product.setReferenceId(referenceId);
        product.setName(name);
        product.setDescription(description);
        product.setImageUrl(imageUrl);
        product.setPrice(price);
        product.setActive(active);

        return productRepository.save(product);
    }

    public void deactivateProduct(
            ProductType type,
            Long referenceId
    ) {

        productRepository
                .findByTypeAndReferenceId(type, referenceId)
                .ifPresent(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                });
    }
}