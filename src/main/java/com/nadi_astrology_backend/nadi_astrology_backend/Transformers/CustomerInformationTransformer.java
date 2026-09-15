package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CustomerInformationRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.CustomerInformation;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import org.springframework.stereotype.Component;

@Component
public class CustomerInformationTransformer {

    public CustomerInformation toEntity(
            CustomerInformationRequest request,
            Order order
    ) {

        return CustomerInformation.builder()
                .order(order)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .birthTime(request.getBirthTime())
                .birthPlace(request.getBirthPlace())
                .countryOfBirth(request.getCountryOfBirth())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .currentLivingCountry(request.getCurrentLivingCountry())
                .question(request.getQuestion())
                .comments(request.getComments())
                .build();
    }
}