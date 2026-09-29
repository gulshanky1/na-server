package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequest {

    @Valid
    @NotNull(message = "Customer information is required")
    private CustomerInformationRequest customerInformation;

    @Valid
    @NotEmpty(message = "At least one product is required")
    @Size(max = 50, message = "Cannot checkout more than 50 products")
    private List<CheckoutItemRequest> items;

    @Valid
    @Size(max = 50, message = "Cannot include more than 50 service details")
    private List<CheckoutServiceDetailsRequest> serviceDetails;
}