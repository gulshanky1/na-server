package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleAuthRequest {

    @NotBlank(message = "Google ID token is required")
    @Size(max = 4096, message = "Google ID token is too long")
    private String idToken;
}