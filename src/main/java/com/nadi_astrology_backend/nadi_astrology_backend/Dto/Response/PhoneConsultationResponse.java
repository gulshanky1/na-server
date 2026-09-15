package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ConsultationType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneConsultationResponse {

    private Long consultationId;

    private String name;

    private ConsultationType consultationType;

    private String shortDescription;

    private String description;

    private String imageUrl;

    private Integer durationMinutes;

    private BigDecimal price;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}