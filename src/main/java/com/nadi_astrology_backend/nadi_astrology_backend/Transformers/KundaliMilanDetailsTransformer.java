package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.KundaliMilanDetailsRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.KundaliMilanDetailsResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.KundaliMilanDetails;
import org.springframework.stereotype.Component;

@Component
public class KundaliMilanDetailsTransformer {

    public KundaliMilanDetails toEntity(
            KundaliMilanDetailsRequest request
    ) {
        return KundaliMilanDetails.builder()
                .boyName(request.getBoyName())
                .boyDateOfBirth(request.getBoyDateOfBirth())
                .boyBirthTime(request.getBoyBirthTime())
                .boyBirthPlace(request.getBoyBirthPlace())
                .girlName(request.getGirlName())
                .girlDateOfBirth(request.getGirlDateOfBirth())
                .girlBirthTime(request.getGirlBirthTime())
                .girlBirthPlace(request.getGirlBirthPlace())
                .build();
    }

    public void updateEntity(
            KundaliMilanDetails details,
            KundaliMilanDetailsRequest request
    ) {
        details.setBoyName(request.getBoyName());
        details.setBoyDateOfBirth(request.getBoyDateOfBirth());
        details.setBoyBirthTime(request.getBoyBirthTime());
        details.setBoyBirthPlace(request.getBoyBirthPlace());

        details.setGirlName(request.getGirlName());
        details.setGirlDateOfBirth(request.getGirlDateOfBirth());
        details.setGirlBirthTime(request.getGirlBirthTime());
        details.setGirlBirthPlace(request.getGirlBirthPlace());
    }

    public KundaliMilanDetailsResponse toResponse(
            KundaliMilanDetails details
    ) {
        return KundaliMilanDetailsResponse.builder()
                .kundaliMilanId(details.getKundaliMilanId())
                .orderItemId(details.getOrderItem().getOrderItemId())
                .boyName(details.getBoyName())
                .boyDateOfBirth(details.getBoyDateOfBirth())
                .boyBirthTime(details.getBoyBirthTime())
                .boyBirthPlace(details.getBoyBirthPlace())
                .girlName(details.getGirlName())
                .girlDateOfBirth(details.getGirlDateOfBirth())
                .girlBirthTime(details.getGirlBirthTime())
                .girlBirthPlace(details.getGirlBirthPlace())
                .createdAt(details.getCreatedAt())
                .updatedAt(details.getUpdatedAt())
                .build();
    }
}
