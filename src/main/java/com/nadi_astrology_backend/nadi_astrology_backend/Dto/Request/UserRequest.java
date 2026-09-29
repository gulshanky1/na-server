package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 150, message = "Full name cannot exceed 150 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 254, message = "Email cannot exceed 254 characters")
    private String email;

    @Pattern(
            regexp = "^\\+?[0-9]{8,15}$",
            message = "Invalid phone number"
    )
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(
            min = 8,
            max = 128,
            message = "Password must contain between 8 and 128 characters"
    )
    private String password;
}