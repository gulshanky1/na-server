package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutServiceDetailsRequest {

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be greater than 0")
    private Long productId;

    /*
     * Informational only.
     * Backend resolves Product -> Service -> ServiceType.
     */
    private ServiceType serviceType;

    @Valid
    private KundaliMilanDetailsRequest kundaliMilan;
}