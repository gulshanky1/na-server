package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponse {

    private Long serviceId;

    private String name;

    private String shortDescription;

    private String description;

    private String imageUrl;

    private BigDecimal price;

    private ServiceType serviceType;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}