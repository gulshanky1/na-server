package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatusRequest {

    @NotNull(message = "Account status is required")
    private Boolean enabled;
}