package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long productId;

    private ProductType type;

    private Long referenceId;

    private String name;

    private String description;

    private String imageUrl;

    private BigDecimal price;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}