package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.PhoneConsultationRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.PhoneConsultationResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.PhoneConsultation;
import org.springframework.stereotype.Component;

@Component
public class PhoneConsultationTransformer {

    public PhoneConsultation toEntity(
            PhoneConsultationRequest request
    ) {

        return PhoneConsultation.builder()
                .name(request.getName())
                .consultationType(request.getConsultationType())
                .shortDescription(request.getShortDescription())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .durationMinutes(request.getDurationMinutes())
                .price(request.getPrice())
                .active(
                        request.getActive() == null
                                || request.getActive()
                )
                .build();
    }

    public void updateEntity(
            PhoneConsultation consultation,
            PhoneConsultationRequest request
    ) {

        consultation.setName(request.getName());
        consultation.setConsultationType(
                request.getConsultationType()
        );
        consultation.setShortDescription(
                request.getShortDescription()
        );
        consultation.setDescription(
                request.getDescription()
        );
        consultation.setDurationMinutes(
                request.getDurationMinutes()
        );
        consultation.setPrice(
                request.getPrice()
        );

        if (request.getImageUrl() != null) {
            consultation.setImageUrl(
                    request.getImageUrl()
            );
        }

        if (request.getActive() != null) {
            consultation.setActive(
                    request.getActive()
            );
        }
    }

    public PhoneConsultationResponse toResponse(
            PhoneConsultation consultation
    ) {

        return PhoneConsultationResponse.builder()
                .consultationId(
                        consultation.getConsultationId()
                )
                .name(
                        consultation.getName()
                )
                .consultationType(
                        consultation.getConsultationType()
                )
                .shortDescription(
                        consultation.getShortDescription()
                )
                .description(
                        consultation.getDescription()
                )
                .imageUrl(
                        consultation.getImageUrl()
                )
                .durationMinutes(
                        consultation.getDurationMinutes()
                )
                .price(
                        consultation.getPrice()
                )
                .active(
                        consultation.isActive()
                )
                .createdAt(
                        consultation.getCreatedAt()
                )
                .updatedAt(
                        consultation.getUpdatedAt()
                )
                .build();
    }
}