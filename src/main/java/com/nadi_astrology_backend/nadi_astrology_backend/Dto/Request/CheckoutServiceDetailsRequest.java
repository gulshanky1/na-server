package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutServiceDetailsRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    /*
     * This is only informational.
     * Backend will NOT trust this value for authorization/validation.
     * Backend will resolve Product -> Service -> ServiceType.
     */
    private ServiceType serviceType;

    @Valid
    private KundaliMilanDetailsRequest kundaliMilan;
}