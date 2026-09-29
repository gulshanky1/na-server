package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResendVerificationRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 254, message = "Email cannot exceed 254 characters")
    private String email;
}